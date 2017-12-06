package gitlet;

import java.io.File;

import static gitlet.Utils.error;

/** Utility class for managing remote repositories in Gitlet.
 *
 *  @author Justin Yang
 */
public final class Remote {

    /** Don't let anyone instantiate this class. */
    private Remote() {
    }

    /** Saves the given login information under the given remote name.
     *
     * @param name the name of the remote repository
     * @param path the path of the remote repository
     */
    public static void add(String name, String path) {
        File file = new File(REMOTE_DIR + name);
        if (file.exists()) {
            throw error("A remote with that name already exists.");
        }
        Utils.writeContents(file, path + "/");
    }

    /** Removes information associated with the given remote name.
     *
     * @param name the name of the remote repository
     */
    public static void remove(String name) {
        File file = new File(REMOTE_DIR + name);
        if (!file.exists()) {
            throw error("A remote with that name does not exist.");
        }
        file.delete();
    }

    /** Removes information associated with the given remote name.
     *
     * @param name the name of the remote repository
     * @param branchName the branch to push
     */
    public static void push(String name, String branchName) {
        File file = new File(REMOTE_DIR + name);
        if (!file.exists()) {
            throw error("A remote with that name does not exist.");
        }
        String repoPath = Utils.readContentsAsString(file);
        if (!new File(repoPath).exists()) {
            throw error("Remote directory not found.");
        }
        if (!Branch.exists(branchName, repoPath)) {
            Commit head = Gitlet.readCurBranch(repoPath).head(repoPath);
            Branch toAdd = new Branch(head, branchName);
            toAdd.writeBranch(repoPath);
        }
        Branch curBranch = Branch.readBranch(branchName);
        Commit curHead = curBranch.head();
        Branch remoteBranch = Branch.readBranch(branchName, repoPath);
        Commit remoteHead = remoteBranch.head(repoPath);

        for (Commit history = curHead;; history = history.parent()) {
            if (history == null) {
                throw error("Please pull down remote changes before pushing.");
            }
            if (history.equals(remoteHead)) {
                break;
            }
        }
        for (Commit history = curHead;
             !history.equals(remoteHead); history = history.parent()) {
            history.writeCommit(repoPath);
        }
        remoteBranch.setHead(curHead.commitID());
        remoteBranch.writeBranch(repoPath);
    }

    /** Brings down commits from the remote Gitlet
     *  repository into the local Gitlet repository.
     *
     * @param name the name of the remote repository
     * @param branchName the branch to fetch
     */
    public static void fetch(String name, String branchName) {
        File file = new File(REMOTE_DIR + name);
        if (!file.exists()) {
            throw error("A remote with that name does not exist.");
        }
        String repoPath = Utils.readContentsAsString(file);
        if (!new File(repoPath).exists()) {
            throw error("Remote directory not found.");
        }
        if (!Branch.exists(branchName, repoPath)) {
            throw error("That remote does not have that branch.");
        }

        Branch remoteBranch = Branch.readBranch(branchName, repoPath);
        Commit remoteHead = remoteBranch.head(repoPath);
        for (Commit history = remoteHead;
             history != null; history = history.parent(repoPath)) {
            if (Commit.exists(history.commitID())) {
                history.writeCommit();
            }
        }

        String fetchName = name + "/" + branchName;
        Branch fetchBranch;
        if (!Branch.exists(fetchName)) {
            fetchBranch = new Branch(remoteHead, fetchName);
        } else {
            fetchBranch = Branch.readBranch(fetchName);
            fetchBranch.setHead(remoteHead.commitID());
        }
        fetchBranch.writeBranch();
    }

    /** Fetches the given branch and merges it into the current branch.
     *
     * @param name the name of the remote repository
     * @param branchName the branch to pull
     */
    public static void pull(String name, String branchName) {
        fetch(name, branchName);
        Gitlet.readCurBranch().merge(Branch.readBranch(name
                + "/" + branchName));
    }

    /** The path that Remote repository names are stored under. */
    static final String REMOTE_DIR = Gitlet.GITLET_DIR + "remote/";
}
