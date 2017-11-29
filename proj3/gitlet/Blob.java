package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;


/** Utility class for managing blobs.
 *
 *  @author Justin Yang
 */
public class Blob {

    /** This class should not be instantiated. */
    private Blob() {}

    @SuppressWarnings("unchecked")
    public void init() {
        if (SERIALIZE.exists()) {
            _blobs = Utils.readObject(serialized, HashMap.class);
        } else {
            _blobs = new HashMap<>();
        }
    }

    public void serialize() {
        Utils.writeObject(new File(METADATA_PATH));
    }

    public boolean equals(Object other) {
        if (other instanceof Blob) {
            return ((Blob) other)._hash.equals(this._hash);
        }
        return false;
    }

    public String retrieveName(String name) {
        return _names.get(name);
    }



    private static HashMap<String, String> _blobs;

    private static final File SERIALIZE = new File(Gitlet.GITLET_DIR + ".blobs");
}
