package gitlet;


import gitlet.Branch;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.Date;

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
        this._commitID = Utils.sha1(this._date.toString());
        this._blobs = new HashMap<>();
        writeCommit();
    }

    /** Constructor for creating a Commit to add to the tree.
     *  Serializes the Commit immediately.
     *
     * @param parent
     * @param message
     */
    @SuppressWarnings("unchecked")
    public Commit(String parent, String message, HashMap<String, Blob> blobs) {
        this._parent = parent;
        this._message = message;
        this._date = new Date();
        this._commitID = Utils.sha1(this._date.toString());
        this._blobs = blobs;
        writeCommit();
    }

    /** Returns the parent of the current commit.
     *
     * @return the parent commit
     */
    public Commit parent() {
        if (_parent == null) {
            return null;
        }
        return readCommit(_parent);
    }

    public String toString() {
        String str = "commit " + _commitID + "\n";
        if (_mergedParent != null) {
            str += "Merge: " + _parent.substring(0, 7) + _mergedParent.substring(0, 7) + "\n";
        }
        str += String.format("Date: %ta %tb %te %tT %tY %tz\n", _date, _date, _date, _date, _date, _date);
        str += _message + "\n\n";
        return str;
    }

    public boolean contains(Blob blob) {
        return _blobs.containsValue(blob);
    }

    public String commitID() {
        return _commitID;
    }

    public String message() {
        return _message;
    }

    public HashMap<String, Blob> blobs() {
        return _blobs;
    }

    public static Commit readCommit(String commitID) {
        return Utils.readObject(new File(COMMIT_DIR + commitID), Commit.class);
    }

    public void writeCommit() {
        Utils.writeObject(new File(COMMIT_DIR + _commitID), this);
    }

    public boolean equals(Object obj) {
        return ((Commit) obj).commitID().equals(commitID());
    }

    private Branch _branch;

    /** The parent commit. */
    private String _parent;

    private String _mergedParent;

    private String _commitID;

    /** The log message. */
    private String _message;

    /** The timestamp. */
    private Date _date;

    private HashMap<String, Blob> _blobs;

    /** The path that Gitlet Commits are stored under. */
    static final String COMMIT_DIR = Gitlet.GITLET_DIR + "commits/";
}
