package gitlet;

import static gitlet.Utils.error;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
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
        this._tracked = head.blobs();
        this._staged = new HashMap<>();
        this._removed = new HashSet<>();
    }

    /** Adds a file to the branch if possible,
     *  saving a copy of the Blob in its current state.
     *
     * @param filename the name of the file to add
     */
    public void add(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            throw error("File does not exist.");
        }

        Blob blob = new Blob(filename);
        if (head().contains(blob)) {
            _staged.remove(filename);
        } else {
            _staged.put(filename, blob);
        }
        _removed.remove(filename);
    }

    /** Creates a Commit of the Branch in its current state.
     *
     * @param message the commit message
     */
    public void commit(String message) {
        if (message == null || message.trim().equals("")) {
            throw error("Please enter a commit message.");
        }
        if (_staged.isEmpty() && _removed.isEmpty()) {
            throw error("No changes added to the commit.");
        }

        for (Map.Entry<String, Blob> entry: _tracked.entrySet()) {
            if (!_staged.containsKey(entry.getKey())) {
                _staged.put(entry.getKey(), entry.getValue());
            }
        }

        for (String filename: _removed) {
            _staged.remove(filename);
        }

        Commit next = new Commit(_headID, null, message, _staged);
        _headID = next.commitID();
        _tracked = _staged;
        _staged = new HashMap<>();
        _removed = new HashSet<>();
    }

    /** Removes a file from _tracked and _commit, as well as the file itself.
     *
     * @param filename the name of the file to remove
     */
    public void remove(String filename) {
        boolean removed = _staged.remove(filename) != null;
        if (_tracked.containsKey(filename)) {
            removed = true;
            _removed.add(filename);
            Utils.restrictedDelete(new File(filename));
        }
        if (!removed) {
            throw error("No reason to remove the file.");
        }
    }

    /** Performs a merge operation.
     *
     * @param other the other Branch to merge with
     */
    public void merge(Branch other) {


    }

    /** Clears the stage. */
    public void clearStage() {
        _staged = new HashMap<>();
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

    /** Returns the staged files.
     *
     * @return the staged files
     */
    public HashMap<String, Blob> staged() {
        return _staged;
    }

    /** Returns the tracked files.
     *
     * @return the tracked files
     */
    public HashMap<String, Blob> tracked() {
        return _tracked;
    }

    /** Returns the removed files.
     *
     * @return the removed files
     */
    public HashSet<String> removed() {
        return _removed;
    }

    /** Returns an ArrayList of modified but unstaged files.
     *
     * @return the ArrayList of modified by unstaged files
     */
    public ArrayList<String> unstaged() {
        ArrayList<String> unstaged = new ArrayList<>();
        for (Blob blob: staged().values()) {
            if (!staged().containsKey(blob.filename())) {
                unstaged.add(checkUnstaged(blob));
            }
        }
        for (Blob blob: staged().values()) {
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
            return blob.filename() + " (deleted)";
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
                    && !staged().containsKey(filename)) {
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

    /** HashMap to hold staged Blobs.
     *  Will be converted into the _tracked upon committing
     *  and be provided for the next Commit as the field _blobs.
     */
    private HashMap<String, Blob> _staged;

    /** HashMap to hold the tracked Blobs.
     *  Same object as _blobs in the Commit identified by _head.
     */
    private HashMap<String, Blob> _tracked;

    /** Tracks the filename of files flagged for removal. */
    private HashSet<String> _removed;

    /** The path that Gitlet Branches are stored under. */
    static final String BRANCH_DIR = Gitlet.GITLET_DIR + "branch/";
}
