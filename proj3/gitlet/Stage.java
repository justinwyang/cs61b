package gitlet;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import static gitlet.Utils.error;

/** Utility class for managing staged and removed files in Gitlet.
 *
 *  @author Justin Yang
 */
public class Stage {

    /** Clears both the staged and empty files,
     * or initializes them if they are null.
     */
    public static void reset() {
        _staged = new HashMap<>();
        _removed = new HashSet<>();
    }

    /** Reads in the Staged metadata to run Gitlet. */
    @SuppressWarnings("unchecked")
    public static void read() {
        _staged = Utils.readObject(new File(Gitlet.GITLET_DIR + ".staged"),
                HashMap.class);
        _removed = Utils.readObject(new File(Gitlet.GITLET_DIR + ".removed"),
                HashSet.class);
    }

    /** Saves the Staged metadata for the next run of Gitlet. */
    public static void write() {
        Utils.writeObject(new File(Gitlet.GITLET_DIR + ".staged"), _staged);
        Utils.writeObject(new File(Gitlet.GITLET_DIR + ".removed"), _removed);
    }

    /** Returns the staged files.
     *
     * @return the staged files
     */
    public static HashMap<String, Blob> staged() {
        return _staged;
    }

    /** Returns the removed files.
     *
     * @return the removed files
     */
    public static HashSet<String> removed() {
        return _removed;
    }

    /** Adds a file to the stage if possible,
     *  saving a copy of the Blob in its current state.
     *
     * @param filename the name of the file to add
     * @param head the current head Commit
     */
    public static void add(String filename, Commit head) {
        File file = new File(filename);
        if (!file.exists()) {
            throw error("File does not exist.");
        }

        Blob blob = new Blob(filename);
        if (head.contains(blob)) {
            staged().remove(filename);
        } else {
            staged().put(filename, blob);
        }
        removed().remove(filename);
    }

    /** Removes a file from staged if possible.
     *  If the file is tracked in the head Commit, it is added
     *  to removed and deleted from the working directory.
     *
     * @param filename the name of the file to remove
     * @param head the current head Commit
     */
    public static void remove(String filename, Commit head) {
        boolean removed = _staged.remove(filename) != null;
        if (head.tracked().containsKey(filename)) {
            removed = true;
            _removed.add(filename);
            Utils.restrictedDelete(new File(filename));
        }
        if (!removed) {
            throw error("No reason to remove the file.");
        }
    }

    /** HashMap to hold staged Blobs.
     *  Will be converted into tracked upon
     *  committing for the next Commit.
     */
    private static HashMap<String, Blob> _staged;

    /** Tracks the filename of files flagged for removal. */
    private static HashSet<String> _removed;
}
