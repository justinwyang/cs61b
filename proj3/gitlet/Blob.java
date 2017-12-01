package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;


/** Utility class for managing blobs.
 *
 *  @author Justin Yang
 */
public class Blob implements Serializable {

    public Blob(String filename) {
        this._filename = filename;
        this._hash = sha1(filename);
    }

    public String filename() {
        return _filename;
    }

    public String hash() {
        return _hash;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Blob) obj).hash().equals(hash());
    }

    public void write() {
        Utils.writeContents(new File(BLOB_PATH + hash()), Utils.readContentsAsString(new File(_filename)));
    }

    public void restore() {

    }

    /** Computes the sha of a file in the working directory.
     *
     * @param filename
     * @return
     */
    public static String sha1(String filename) {
        File file = new File(filename);
        return Utils.sha1(file.getName(), Utils.readContents(file));
    }

    /** The Blob's filename. */
    private String _filename;

    /** The Blob's hash. */
    private String _hash;

    /** The path to store the blobs in when adding a file. */
    static final String BLOB_PATH = Gitlet.GITLET_DIR + "blobs/";
}
