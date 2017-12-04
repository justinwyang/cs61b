package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
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
        this._blobs = new HashMap<>();
        this._commitID = Utils.sha1(this._date.toString(), _blobs.toString());
        writeCommit();
    }

    /** Constructor for creating a Commit to add to the tree.
     *  Serializes the Commit immediately.
     *
     * @param parent the parent Commit
     * @param mergedParent the parent merged into the Commit
     * @param message the Commit message
     * @param blobs the blobs to include
     */
    @SuppressWarnings("unchecked")
    public Commit(String parent, String mergedParent,
                  String message, HashMap<String, Blob> blobs) {
        this._parent = parent;
        this._mergedParent = _mergedParent;
        this._message = message;
        this._date = new Date();
        this._blobs = blobs;
        this._commitID = Utils.sha1(this._date.toString(), _blobs.toString());
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
        for (Blob blob: _blobs.values()) {
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
        return _blobs.containsValue(blob);
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

    /** Returns the stored Blobs.
     *
     * @return the stored Blobs.
     */
    public HashMap<String, Blob> blobs() {
        return _blobs;
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

    /** The stored Blob. */
    private HashMap<String, Blob> _blobs;

    /** The path that Gitlet Commits are stored under. */
    static final String COMMIT_DIR = Gitlet.GITLET_DIR + "commits/";
}