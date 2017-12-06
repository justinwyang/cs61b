package gitlet;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Consumer;

import static gitlet.Utils.error;

/** Controls the main processes of Gitlet.
 *
 *  Acknowledgements: Much of the class structure, including
 *  the HashMap of commands, was borrowed from Game.java from
 *  Qirkat.
 *
 *  @author Justin Yang
 */
public class Gitlet {

    /** Processes and executes a Gitlet command.
     *
     * @param command the commands to process and execute.
     * @param operands the included operands
     */
    public void process(String command, String[] operands) {
        if (!_commands.containsKey(command)) {
            throw error("No command with that name exists.");
        }
        File gitletDir = new File(GITLET_DIR);
        if (command.equals("init")) {
            if (gitletDir.exists()) {
                throw error("A Gitlet version-control system "
                        + "already exists in the current directory.");
            }
        } else {
            if (gitletDir.exists()) {
                read();
            } else {
                throw error("Not in an initialized Gitlet directory.");
            }
        }
        _commands.get(command).accept(operands);
        write();
    }

    /** Reads in the metadata to run Gitlet. */
    public void read() {
        _branch = readCurBranch();
        Stage.read();
    }

    /** Saves the metadata for the next run of Gitlet. */
    public void write() {
        _branch.writeBranch();
        Utils.writeContents(new File(GITLET_DIR + ".branch"), _branch.name());
        Stage.write();
    }

    /** Checks for the correct number of operands.
     *
     * @param operands the operands to check
     * @param num the required number of operands
     */
    public void checkOperands(String[] operands, int num) {
        if (num != operands.length) {
            throw error(INCORRECT_OPERANDS);
        }
    }

    /** Performs an init command.
     *
     * @param operands operands for init
     */
    public void init(String[] operands) {
        checkOperands(operands, 0);
        File gitletDir = new File(GITLET_DIR);
        gitletDir.mkdir();
        File branchDir = new File(Branch.BRANCH_DIR);
        branchDir.mkdir();
        File commitDir = new File(Commit.COMMIT_DIR);
        commitDir.mkdir();
        File blobDir = new File(Blob.BLOB_DIR);
        blobDir.mkdir();
        File remoteDir = new File(Remote.REMOTE_DIR);
        remoteDir.mkdir();
        Commit head = new Commit();
        _branch = new Branch(head, "master");
        Stage.reset();
    }

    /** Performs an add command.
     *
     * @param operands operands for add
     */
    public void add(String[] operands) {
        checkOperands(operands, 1);
        Stage.add(operands[0], _branch.head());
    }

    /** Performs a commit command.
     *
     * @param operands operands for commit
     */
    public void commit(String[] operands) {
        checkOperands(operands, 1);
        _branch.commit(operands[0], null);
    }

    /** Performs a rm command.
     *
     * @param operands operands for rm
     */
    public void remove(String[] operands) {
        checkOperands(operands, 1);
        Stage.remove(operands[0], _branch.head());
    }

    /** Performs a log command.
     *
     * @param operands operands for log
     */
    public void log(String[] operands) {
        checkOperands(operands, 0);
        for (Commit commit = _branch.head();
             commit != null; commit = commit.parent()) {
            System.out.print(commit);
        }
    }

    /** Performs a globalLog command.
     *
     * @param operands operands for globalLog
     */
    public void globalLog(String[] operands) {
        checkOperands(operands, 0);
        for (String commitID: Utils.plainFilenamesIn(Commit.COMMIT_DIR)) {
            System.out.print(Commit.readCommit(commitID));
        }
    }

    /** Performs a find command.
     *
     * @param operands operands for find
     */
    public void find(String[] operands) {
        checkOperands(operands, 1);
        boolean found = false;
        for (String commitID: Utils.plainFilenamesIn(Commit.COMMIT_DIR)) {
            Commit commit = Commit.readCommit(commitID);
            if (commit.message().equals(operands[0])) {
                System.out.println(commit.commitID());
                found = true;
            }
        }
        if (!found) {
            throw error("Found no commit with that message.");
        }
    }

    /** Performs a status command.
     *
     * @param operands operands for status
     */
    public void status(String[] operands) {
        checkOperands(operands, 0);

        sortAndPrint(Utils.plainFilenamesIn(Branch.BRANCH_DIR),
                "=== Branches ===", true);

        sortAndPrint(new ArrayList<>(Stage.staged().keySet()),
                "=== Staged Files ===", false);

        sortAndPrint(new ArrayList<>(Stage.removed()),
                "=== Removed Files ===", false);

        sortAndPrint(_branch.unstaged(),
                "=== Modifications Not Staged For Commit ===", false);

        sortAndPrint(_branch.untracked(),
                "=== Untracked Files ===", false);
    }

    /** Sorts the entries of list and prints them for status.
     *
     * @param list the list to sort and print
     * @param header the header label
     * @param asterisk whether to add asterisk before the current branch
     */
    private void sortAndPrint(List<String> list,
                              String header, boolean asterisk) {
        System.out.println(header);
        Collections.sort(list);
        for (String entry: list) {
            if (entry.equals("")) {
                continue;
            }
            if (asterisk && _branch.name().equals(entry)) {
                System.out.print("*");
            }
            System.out.println(entry);
        }
        System.out.println();
    }

    /** Performs a checkout command.
     *
     * @param operands operands for checkout
     */
    public void checkout(String[] operands) {
        if (operands.length == 2 && operands[0].equals("--")) {
            checkoutCommitFile(_branch.head().commitID(), operands[1]);
        } else if (operands.length == 3 && operands[1].equals("--")) {
            checkoutCommitFile(operands[0], operands[2]);
        } else if (operands.length == 1) {
            if (!Branch.exists(operands[0])) {
                throw error("No such branch exists.");
            }
            if (operands[0].equals(_branch.name())) {
                throw error("No need to checkout the current branch.");
            }
            checkoutBranch(operands[0],
                    Branch.readBranch(operands[0]).head().commitID());
        } else {
            throw error(INCORRECT_OPERANDS);
        }
    }

    /** Checks out a file from a specific Commit.
     *
     * @param prefix The CommitID prefix to checkout from
     * @param filename The filename to checkout
     */
    private void checkoutCommitFile(String prefix, String filename) {
        String found = Commit.findCommit(prefix);
        HashMap<String, Blob> blobs = Commit.readCommit(found).tracked();
        if (!blobs.containsKey(filename)) {
            throw error("File does not exist in that commit.");
        }
        blobs.get(filename).checkout();
    }

    /** Checks out a Branch.
     *
     * @param branchName the Branch name to checkout
     * @param commitID the CommitID to checkout from
     */
    public void checkoutBranch(String branchName, String commitID) {
        Commit commit = Commit.readCommit(commitID);
        _branch.checkOverwiteUntracked(commit);
        for (String filename: _branch.tracked().keySet()) {
            if (!commit.tracked().keySet().contains(filename)) {
                Utils.restrictedDelete(filename);
            }
        }
        commit.checkout();
        _branch.writeBranch();
        _branch = Branch.readBranch(branchName);
        Stage.reset();
    }

    /** Performs a branch command.
     *
     * @param operands operands for branch
     */
    public void branch(String[] operands) {
        checkOperands(operands, 1);
        if (Branch.exists(operands[0])) {
            throw error("A branch with that name already exists.");
        }
        Branch other =
                new Branch(_branch.head(), operands[0]);
        other.writeBranch();
    }

    /** Performs a rmBranch command.
     *
     * @param operands operands for rmBranch
     */
    public void rmBranch(String[] operands) {
        checkOperands(operands, 1);
        if (operands[0].equals(_branch.name())) {
            throw error("Cannot remove the current branch.");
        }
        File file = new File(Branch.BRANCH_DIR + operands[0]);
        if (!file.exists()) {
            throw error("A branch with that name does not exist.");
        }
        file.delete();
    }

    /** Performs a reset command.
     *
     * @param operands operands for reset
     */
    public void reset(String[] operands) {
        checkOperands(operands, 1);
        String commitID = Commit.findCommit(operands[0]);
        checkoutBranch(_branch.name(), commitID);
        _branch.setHead(commitID);
    }

    /** Performs a merge command.
     *
     * @param operands operands for merge
     */
    public void merge(String[] operands) {
        checkOperands(operands, 1);
        if (!Stage.staged().isEmpty() || !Stage.removed().isEmpty()) {
            throw error("You have uncommitted changes.");
        }
        if (!Branch.exists(operands[0])) {
            throw error("A branch with that name does not exist.");
        }
        if (_branch.name().equals(operands[0])) {
            throw error("Cannot merge a branch with itself.");
        }
        if (_branch.merge(Branch.readBranch(operands[0]))) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    /** Saves the given login information under the given remote name.
     *
     * @param operands operands for addRemote
     */
    public void addRemote(String[] operands) {
        checkOperands(operands, 2);
        Remote.add(operands[0], operands[1]);
    }

    /** Saves the given login information under the given remote name.
     *
     * @param operands operands for rmRemote
     */
    public void rmRemote(String[] operands) {
        checkOperands(operands, 1);
        Remote.remove(operands[0]);
    }

    /** Attempts to append the current branch's commits to the
     *  end of the given branch at the given remote repository.
     *
     * @param operands operands for push
     */
    public void push(String[] operands) {
        checkOperands(operands, 2);
        Remote.push(operands[0], operands[1]);
    }

    /** Brings down commits from the remote Gitlet
     *  repository into the local Gitlet repository.
     *
     * @param operands operands for fetch
     */
    public void fetch(String[] operands) {
        checkOperands(operands, 2);
        Remote.fetch(operands[0], operands[1]);
    }

    /** Fetches the given branch and merges it into the current branch.
     *
     * @param operands operands for pull
     */
    public void pull(String[] operands) {
        checkOperands(operands, 2);
        Remote.fetch(operands[0], operands[1]);
        _branch.merge(Branch.readBranch(operands[0]
                + "/" + operands[1]));
    }

    /** Returns the current Branch from the given repository.
     *
     * @param repoPath the path of the repository
     * @return the current Branch
     */
    public static Branch readCurBranch(String repoPath) {
        return Branch.readBranch(Utils.readContentsAsString
                (new File(repoPath + ".branch")));
    }

    /** Returns the current Branch from the current repository.
     *
     * @return the current Branch
     */
    public static Branch readCurBranch() {
        return readCurBranch(GITLET_DIR);
    }

    /** Mapping of commands to methods that process them. */
    private final HashMap<String, Consumer<String[]>> _commands =
            new HashMap<>(); {
        _commands.put("init", this::init);
        _commands.put("add", this::add);
        _commands.put("commit", this::commit);
        _commands.put("rm", this::remove);
        _commands.put("log", this::log);
        _commands.put("global-log", this::globalLog);
        _commands.put("find", this::find);
        _commands.put("status", this::status);
        _commands.put("checkout", this::checkout);
        _commands.put("branch", this::branch);
        _commands.put("rm-branch", this::rmBranch);
        _commands.put("reset", this::reset);
        _commands.put("merge", this::merge);
        _commands.put("add-remote", this::addRemote);
        _commands.put("rm-remote", this::rmRemote);
        _commands.put("push", this::push);
        _commands.put("fetch", this::fetch);
        _commands.put("pull", this::pull);
    }

    /** The current Branch. */
    private Branch _branch;

    /** The path for the main Gitlet directory. */
    static final String GITLET_DIR = ".gitlet/";

    /** Error message for incorrect operands. */
    private static final String INCORRECT_OPERANDS = "Incorrect operands.";
}
