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

    /** Returns the parent of the current commit.
     *
     * @return the parent commit
     */
    public Commit parent() {
        return _parent;
    }

    public Commit retrieve(String commitID) {
        Commit commit;
        File inFile = new File(Gitlet.GITLET_DIR + commitID + "/.metadata");
        try {
            ObjectInputStream inp =
                    new ObjectInputStream(new FileInputStream(inFile));
            commit = (Commit) inp.readObject();
            inp.close();
        } catch (IOException e) {
            throw new GitletException("IOException retrieving commit.");
        } catch (ClassNotFoundException e) {
            throw new GitletException("ClassNotFoundException retrieving commit.");
        }
        _commits.put(commitID, commit);
        return commit;
    }

    /** The parent commit. */
    private Commit _parent;

    /** The log message. */
    private String _message;

    /** The timestamp. */
    private String _timestamp;

    /** The tree mapping of this commit. */
    private Tree _tree;

    /** Stores all the commits currently in ure. */
    private static HashMap<String, Commit> _commits;
}
