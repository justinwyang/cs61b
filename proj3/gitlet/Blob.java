package gitlet;

import java.io.File;

/** Represents a Blob object.
 *
 *  @author Justin Yang
 */
public class Blob {

    /** Constructor for Blob, which also saves a copy of the file in its current state.
     *
     * @param filename the filename of the Blob to be created.
     */
    public Blob(String filename) {
        this._filename = filename;
        this._hash = sha1(filename);
        Utils.writeContents(new File(BLOB_DIR + hash()), Utils.readContentsAsString(new File(_filename)));
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

    @Override
    public boolean equals(Object obj) {
        return ((Blob) obj).hash().equals(hash());
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
