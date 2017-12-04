package gitlet;

import java.io.File;
import java.io.Serializable;

/** Represents a Blob object.
 *
 *  @author Justin Yang
 */
public class Blob implements Serializable {

    /** Constructor for Blob, which also saves a copy of the
     *  file in its current state.
     *
     * @param filename the filename of the Blob to be created.
     */
    public Blob(String filename) {
        this._filename = filename;
        this._hash = sha1(filename);
        Utils.writeContents(new File(BLOB_DIR + hash()),
                Utils.readContentsAsString(new File(_filename)));
    }

    /** Restores the version saved in the current
     *  blob into the working directory. */
    public void restore() {
        Utils.writeContents(new File(_filename),
                Utils.readContentsAsString(new File(BLOB_DIR + hash())));
    }

    /** Returns the filename of the current Blob.
     *
     * @return the filename of the current Blob.
     */
    public String filename() {
        return _filename;
    }

    /** Calculates the hash for the current Blob based on the filename.
     *
     * @return the hash for the current Blob.
     */
    public String hash() {
        return _hash;
    }

    /** Returns the String representation, given by its hash.
     *  Used when obtaining the String representation of _blobs for Commit,
     *  which helps differentiate between different Commits made at
     *  nearly the same time.
     *
     * @return the hash value of the Blob.
     */
    public String toString() {
        return hash();
    }

    @Override
    public boolean equals(Object obj) {
        return ((Blob) obj).hash().equals(hash());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /** Computes the sha of a file in the working directory.
     *
     * @param filename the file whose sha1 value is to be determined
     * @return the sha1 value of filename
     */
    public static String sha1(String filename) {
        File file = new File(filename);
        return Utils.sha1(file.getName(), Utils.readContents(file));
    }

    /** The Blob's filename. */
    private String _filename;

    /** THe Blob's hash value. */
    private String _hash;

    /** The path that Gitlet Blobs are stored under. */
    static final String BLOB_DIR = Gitlet.GITLET_DIR + "blobs/";
}
