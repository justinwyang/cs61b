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
        this._mergedParent = _mergedParent;
        this._message = message;
        this._date = new Date();
        this._tracked = tracked;
        this._commitID = Utils.sha1(this._date.toString(), _tracked.toString(), this._parent);
        writeCommit();
    }

    /** Returns the parent Commit.
     *
     * @return the parent commit
     */
    public Commit parent() {
        if (_parent == null) {
            return null;
        }
        return readCommit(_parent);
    }

    /** Restores the contents of the commit to the working directory. */
    public void restore() {
        for (Blob blob: _tracked.values()) {
            blob.restore();
        }
    }

    /** Returns the String representation.
     *
     * @return the String representation
     */
    public String toString() {
        String str = "===" + "\n" + "commit " + _commitID + "\n";
        if (_mergedParent != null) {
            str += "Merge: " + _parent.substring(0, 7)
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
     * @param given the second (given) Commit
     * @return the split point CommitID
     */
    public static Commit findSplitPoint(Commit current, Commit given) {
        HashSet<String> commits = new HashSet<>();
        for (; current != null; current = current.parent()) {
            commits.add(current.commitID());
        }
        for (; given != null; given = given.parent()) {
            if (commits.contains(given)) {
                break;
            }
        }
        return given;
    }

    /** Reads in a Commit by its CommitID and returns it.
     *
     * @param commitID the CommitID of the Commit to read
     * @return the unserialized Commit
     */
    public static Commit readCommit(String commitID) {
        return Utils.readObject(new File(COMMIT_DIR + commitID), Commit.class);
    }

    /** Serializes the current Commit. */
    public void writeCommit() {
        Utils.writeObject(new File(COMMIT_DIR + _commitID), this);
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
