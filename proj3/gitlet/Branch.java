package gitlet;

import static gitlet.Utils.error;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/** Represents a Branch object.
 *
 *  @author Justin Yang
 */
public class Branch implements Serializable {

    /** Constructor for Branch.
     *
     * @param head the head Commit for the Branch
     * @param name the name for the Branch
     */
    public Branch(Commit head, String name) {
        this._headID = head.commitID();
        this._name = name;
    }

    /** Creates a Commit of the Branch in its current state.
     *
     * @param message the commit message
     */
    public void commit(String message) {
        if (message == null || message.trim().equals("")) {
            throw error("Please enter a commit message.");
        }
        if (Stage.staged().isEmpty() && Stage.removed().isEmpty()) {
            throw error("No changes added to the commit.");
        }

        for (Map.Entry<String, Blob> entry: tracked().entrySet()) {
            if (!Stage.staged().containsKey(entry.getKey())) {
                Stage.staged().put(entry.getKey(), entry.getValue());
            }
        }

        for (String filename: Stage.removed()) {
            Stage.staged().remove(filename);
        }

        Commit next = new Commit(_headID, null, message, Stage.staged());
        _headID = next.commitID();
        Stage.reset();

    }

    /** Performs a merge operation.
     *
     * @param otherBranch the other Branch to merge with
     */
    public void merge(Branch otherBranch) {
        Commit commit = head();
        Commit other = otherBranch.head();
        Commit split = Commit.findSplitPoint(commit, other);
        if (split.equals(otherBranch.head())) {
            System.out.println("Given branch is an ancestor"
                    + " of the current branch.");
            return;
        }
        if (split.equals(head())) {
            setHead(otherBranch.head().commitID());
            System.out.println("Current branch fast-forwarded.");
            return;
        }

        for (Map.Entry<String, Blob> entry: split.tracked().entrySet()) {
            String filename = entry.getKey();
            Blob blob = entry.getValue();


        }


    }

    /** Returns the name.
     *
     * @return the name
     */
    public String name() {
        return _name;
    }

    /** Returns the head Commit.
     *
     * @return the head Commit
     */
    public Commit head() {
        return Commit.readCommit(_headID);
    }

    /** Sets the headID to a new value.
     *
     * @param headID the headID to set to
     */
    public void setHead(String headID) {
        this._headID = headID;
    }

    /** Returns the tracked files.
     *
     * @return the tracked files
     */
    public HashMap<String, Blob> tracked() {
        return head().tracked();
    }

    /** Returns an ArrayList of modified but unstaged files.
     *
     * @return the ArrayList of modified by unstaged files
     */
    public ArrayList<String> unstaged() {
        ArrayList<String> unstaged = new ArrayList<>();
        for (Blob blob: tracked().values()) {
            if (!Stage.staged().containsKey(blob.filename())) {
                unstaged.add(checkUnstaged(blob));
            }
        }
        for (Blob blob: Stage.staged().values()) {
            unstaged.add(checkUnstaged(blob));
        }
        return unstaged;
    }

    /** Checks if a Blob is unstaged, and if it is, returns
     *  the respective description along with the filename,
     *  otherwise returns a blank String.
     *
     * @param blob the Blob to check
     * @return the filename and description if unstaged
     */
    private String checkUnstaged(Blob blob) {
        if (!new File(blob.filename()).exists()) {
            if (!Stage.removed().contains(blob.filename())) {
                return blob.filename() + " (deleted)";
            }
            return "";
        }
        if (!blob.hash().equals(Blob.sha1(blob.filename()))) {
            return blob.filename() + " (modified)";
        }
        return "";
    }

    /** Returns an ArrayList of untracked files.
     *
     * @return the ArrayList of untracked files
     */
    public ArrayList<String> untracked() {
        ArrayList<String> untracked = new ArrayList<>();
        for (String filename: Utils.plainFilenamesIn(".")) {
            if (!tracked().containsKey(filename)
                    && !Stage.staged().containsKey(filename)) {
                untracked.add(filename);
            }
        }
        return untracked;
    }

    /** Returns whether a Branch with the given name exists.
     *
     * @param branchName the Branch name to check
     * @return whether the branch exists or not
     */
    public static boolean exists(String branchName) {
        return new File(BRANCH_DIR + branchName).exists();
    }

    /** Reads in a Branch by its name and returns it.
     *
     * @param branchName the name of the Branch to read
     * @return the unserialized Branch
     */
    public static Branch readBranch(String branchName) {
        return Utils.readObject(new File(BRANCH_DIR + branchName),
                Branch.class);
    }

    /** Serializes the current Branch. */
    public void writeBranch() {
        Utils.writeObject(new File(BRANCH_DIR + _name), this);
    }

    /** The Branch's name, which it will be identified by. */
    private String _name;

    /** CommitID of the head commit. */
    private String _headID;

    /** The path that Gitlet Branches are stored under. */
    static final String BRANCH_DIR = Gitlet.GITLET_DIR + "branch/";
}
