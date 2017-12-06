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
        write(Gitlet.GITLET_DIR, Gitlet.GITLET_DIR);
    }

    /** Writes the contents of the Blob from
     *  one repository to the other.
     *
     * @param fromRepo the path of the from repository
     * @param toRepo the path of the to repository
     */
    public void write(String fromRepo, String toRepo) {
        fromRepo = fromRepo.substring(0, fromRepo.length() - 8);
        if (!new File(toRepo + hash()).exists()) {
            Utils.writeContents(new File(toRepo + "blobs/" + hash()),
                    Utils.readContentsAsString(new File(fromRepo + _filename)));
        }
    }

    /** Restores the version saved in the current
     *  blob into the working directory. */
    public void checkout() {
        Utils.writeContents(new File(_filename), contents());
    }

    /** Returns the contents of the Blob as a String.
     *
     * @return the contents of the Blob
     */
    public String contents() {
        return Utils.readContentsAsString(new File(BLOB_DIR + hash()));
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
