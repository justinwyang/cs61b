package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Date;

import static gitlet.Utils.error;

/** Represents a Commit object.
 *
 *  @author Justin Yang
 */
public class Commit implements Serializable {

    /** Constructor for initial Commit.
     *  Serializes the Commit immediately.
     */
    public Commit() {
        this._parent = null;
        this._message = "initial commit";
        this._date = new Date(0L);
        this._tracked = new HashMap<>();
        this._commitID = Utils.sha1(this._date.toString(), _tracked.toString());
        writeCommit();
    }

    /** Constructor for creating a Commit to add to the tree.
     *  Serializes the Commit immediately.
     *
     * @param parent the parent Commit
     * @param mergedParent the parent merged into the Commit
     * @param message the Commit message
     * @param tracked the blobs to include
     */
    public Commit(String parent, String mergedParent,
                  String message, HashMap<String, Blob> tracked) {
        this._parent = parent;
        this._mergedParent = mergedParent;
        this._message = message;
        this._date = new Date();
        this._tracked = tracked;
        this._commitID = Utils.sha1(this._date.toString(),
                _tracked.toString(), this._parent);
        writeCommit();
    }

    /** Returns the parent Commit from the given repository.
     *
     * @param repoPath the path of the repository
     * @return the parent commit
     */
    public Commit parent(String repoPath) {
        if (_parent == null) {
            return null;
        }
        return readCommit(_parent, repoPath);
    }

    /** Returns the parent Commit from the current repository.
     *
     * @return the parent commit
     */
    public Commit parent() {
        return parent(Gitlet.GITLET_DIR);
    }

    /** Restores the contents of the commit to the working directory. */
    public void checkout() {
        for (Blob blob: _tracked.values()) {
            blob.checkout();
        }
    }

    /** Adds absent blobs from one repository to the other.
     *
     * @param fromRepo the path of the from repository
     * @param toRepo the path of the to repository
     */
    public void refreshBlobs(String fromRepo, String toRepo) {
        for (Blob blob: _tracked.values()) {
            blob.write(fromRepo, toRepo);
        }
    }

    /** Returns the String representation.
     *
     * @return the String representation
     */
    public String toString() {
        String str = "===" + "\n" + "commit " + _commitID + "\n";
        if (_mergedParent != null) {
            str += "Merge: " + _parent.substring(0, 7) + " "
                    + _mergedParent.substring(0, 7) + "\n";
        }
        str += String.format("Date: %ta %tb %te %tT %tY %tz\n",
                _date, _date, _date, _date, _date, _date);
        str += _message + "\n\n";
        return str;
    }

    /** Returns whether the Commit contains a blob.
     *
     * @param blob the Blob to check for
     * @return whether the Commit contains the blob
     */
    public boolean contains(Blob blob) {
        return _tracked.containsValue(blob);
    }

    /** Returns the CommitID.
     *
     * @return the CommitID
     */
    public String commitID() {
        return _commitID;
    }

    /** Returns the Commit message.
     *
     * @return the Commit message
     */
    public String message() {
        return _message;
    }

    /** Returns the tracked Blobs.
     *
     * @return the tracked Blobs
     */
    public HashMap<String, Blob> tracked() {
        return _tracked;
    }

    /** Searches for a commit with the given CommitID prefix,
     *  and throw an exception otherwise.
     *
     * @param prefix the prefix to search for
     * @return the CommitID found
     */
    public static String findCommit(String prefix) {
        if (new File(COMMIT_DIR + prefix).exists()) {
            return prefix;
        }
        for (String commit: Utils.plainFilenamesIn(Commit.COMMIT_DIR)) {
            if (commit.substring(0, prefix.length()).equals(prefix)) {
                return commit;
            }
        }
        throw error("No commit with that id exists.");
    }

    /** Searches for the split point CommitID of the two Commits.
     *
     * @param current the first (current) Commit
     * @param other the second (given) Commit
     * @return the split point CommitID
     */
    public static Commit findSplitPoint(Commit current, Commit other) {
        HashSet<String> commits = new HashSet<>();
        for (; current != null; current = current.parent()) {
            commits.add(current.commitID());
        }
        for (; other != null; other = other.parent()) {
            if (commits.contains(other.commitID())) {
                break;
            }
        }
        return other;
    }

    /** Returns the union of all the filenames of the given commits.
     *
     * @param commits the commits to take filenames from
     * @return the union of all the filenames
     */
    public static HashSet<String> union(Commit... commits) {
        HashSet<String> union = new HashSet<>();
        for (Commit commit: commits) {
            union.addAll(commit.tracked().keySet());
        }
        return union;
    }

    /** Writes a file that is in merge conflict.
     *
     * @param current the current Commit
     * @param other the other Commit
     * @param filename the filename in question
     */
    public static void mergeConflict(Commit current,
                                     Commit other, String filename) {
        String contents = "<<<<<<< HEAD\n";
        if (current.tracked().containsKey(filename)) {
            contents += current.tracked().get(filename).contents();
        }
        contents += "=======\n";
        if (other.tracked().containsKey(filename)) {
            contents += other.tracked().get(filename).contents();
        }
        contents += ">>>>>>>\n";
        Utils.writeContents(new File(filename), contents);
        Stage.add(filename, current);
    }

    /** Returns whether a Commit with the given
     *  CommitID exists in the given repository.
     *
     * @param commitID the CommitID to check
     * @param repoPath the path of the repository
     * @return whether the Commit exists or not
     */
    public static boolean exists(String commitID, String repoPath) {
        return new File(repoPath + "commits/" + commitID).exists();
    }

    /** Returns whether a Commit with the given
     *  CommitID exists in the current repository.
     *
     * @param commitID the Branch name to check
     * @return whether the branch exists or not
     */
    public static boolean exists(String commitID) {
        return exists(commitID, Gitlet.GITLET_DIR);
    }

    /** Reads in a Commit by its CommitID from
     * the given repository and returns it.
     *
     * @param commitID the CommitID of the Commit to read
     * @param repoPath the path of the repository
     * @return the unserialized Commit
     */
    public static Commit readCommit(String commitID, String repoPath) {
        return Utils.readObject(new File(repoPath
                + "commits/" + commitID), Commit.class);
    }

    /** Reads in a Commit by its CommitID from
     * the curre t repository and returns it.
     *
     * @param commitID the CommitID of the Commit to read
     * @return the unserialized Commit
     */
    public static Commit readCommit(String commitID) {
        return readCommit(commitID, Gitlet.GITLET_DIR);
    }

    /** Serializes the current Commit in the given repository.
     *
     * @param repoPath the path of the repository
     */
    public void writeCommit(String repoPath) {
        Utils.writeObject(new File(repoPath + "commits/" + _commitID), this);
    }

    /** Serializes the current Commit in the current repository. */
    public void writeCommit() {
        writeCommit(Gitlet.GITLET_DIR);
    }

    @Override
    public boolean equals(Object obj) {
        return ((Commit) obj).commitID().equals(commitID());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /** The CommitId of the parent commit. */
    private String _parent;

    /** The CommitID of the merged parent (null if it doesn't exist). */
    private String _mergedParent;

    /** The CommitID. */
    private String _commitID;

    /** The log message. */
    private String _message;

    /** The timestamp. */
    private Date _date;

    /** The tracked Blobs. */
    private HashMap<String, Blob> _tracked;

    /** The path that Gitlet Commits are stored under. */
    static final String COMMIT_DIR = Gitlet.GITLET_DIR + "commits/";
}
