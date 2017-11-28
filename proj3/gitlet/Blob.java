package gitlet;

import java.io.File;
import java.io.Serializable;


/** Represents the contents of files.
 *
 *  @author Justin Yang
 */
public class Blob implements Serializable {

    public Blob(String filename) {
        this._filename = filename;
        this._hash = Utils.sha1(_filename, Utils.readContents(new File(_filename)));
    }

    public boolean equals(Object other) {
        if (other instanceof Blob) {
            return ((Blob) other)._hash.equals(this._hash);
        }
        return false;
    }

    /** The filename this current Blob tracks. */
    private String _filename;

    /** The hash value for this blob. */
    private String _hash;
}
