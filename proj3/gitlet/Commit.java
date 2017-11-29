package gitlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.HashMap;

/** Represents a Commit object.
 *
 *  @author Justin Yang
 */

public class Commit implements Serializable {

    public Commit(String parent, String message, String timestamp) {
        this._parent = parent;
        this._message = message;
        this._timestamp = timestamp;
    }

    /** Returns the parent of the current commit.
     *
     * @return the parent commit
     */
    public String parent() {
        return _parent;
    }

    public static Commit readCommit(String commitID) {
//        Commit commit = Utils.readObject(new File(_METADATA_PATH), Commit.class);
//        try {
//            ObjectInputStream inp =
//                    new ObjectInputStream(new FileInputStream(_METADATA_PATH));
//            commit = (Commit) inp.readObject();
//            inp.close();
//        } catch (IOException e) {
//            throw new GitletException("IOException retrieving commit.");
//        } catch (ClassNotFoundException e) {
//            throw new GitletException("ClassNotFoundException retrieving commit.");
//        }



//        _commits.put(commitID, commit);
        return null;
    }

    public void writeCommit() {
        Utils.writeObject(new File(_METADATA_PATH), this);
    }

    private Branch _branch;

    /** The parent commit. */
    private String _parent;

    /** The log message. */
    private String _message;

    /** The timestamp. */
    private String _timestamp;

    /** Stores all the commits currently in use. */
    private HashMap<String, String> _tree;

    /** Stores the path of the commit metadata. */
    private final String _COMMIT_PATH = Gitlet.GITLET_DIR + ".commit";

    private final String _METADATA_PATH = Gitlet.GITLET_DIR + ".metadata";
}
