package gitlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;

/** Represents a Commit object.
 *
 *  @author Justin Yang
 */

public class Commit implements Serializable {

    /** Constructor for initial commit. */
    public Commit() {
        this._parent = null;
        this._message = "initial commit";
        this._date = new Date(0L);
        this._commitID = Utils.sha1(this._date.toString());
        this._blobs = new HashSet<>();
    }

    /** Constructor for adding a commit to the tree.
     *
     * @param parent
     * @param message
     */
    @SuppressWarnings("unchecked")
    public Commit(String parent, String message) {
        this._parent = parent;
        this._message = message;
        this._date = new Date();
        this._commitID = Utils.sha1(this._date.toString());
        this._blobs = (HashSet<String>) parent()._blobs.clone();
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

    public static Commit readCommit(String commitID) {
        return Utils.readObject(new File(COMMIT_DIR + commitID), Commit.class);
    }

    ///Finish later
    public String toString() {
        String str = "commit " + _commitID + "\n";
        if (_mergedParent != null) {
            str += "Merge: " + _parent.substring(0, 7) + _mergedParent.substring(0, 7) + "\n";
        }
//        str += String.format("Date: %s %s %d %d:%d:%d %d ")

        return str;
    }

    // change later?
    public boolean contains(File file) {
        return _blobs.contains(Blob.sha1(file));
    }

//    public void writeCommit() {
//        Utils.writeObject(new File(_METADATA_PATH), this);
//    }


    private Branch _branch;

    /** The parent commit. */
    private String _parent;

    private String _mergedParent;

    private String _commitID;

    /** The log message. */
    private String _message;

    /** The timestamp. */
    private Date _date;

    private HashSet<String> _blobs;

    private static final String COMMIT_DIR = Gitlet.GITLET_DIR + "commits/";
}
