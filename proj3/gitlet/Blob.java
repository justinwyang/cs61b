package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;


/** Utility class for managing blobs.
 *
 *  @author Justin Yang
 */
public class Blob {

    @SuppressWarnings("unchecked")
    public void init() {
        if (SERIALIZED_FILE.exists()) {
            _blobs = Utils.readObject(SERIALIZED_FILE, HashMap.class);
        } else {
            _blobs = new HashMap<>();
        }
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


    public void serialize() {
        Utils.writeObject(SERIALIZED_FILE, _blobs);
    }

    public static void add(String filename) {
        File file = new File(filename);
        _blobs.put(sha1(filename), file.getPath());
    }

    public String retrieveName(String hash) {
        return _blobs.get(hash);
    }

    private static HashMap<String, String> _blobs;

    private static final File SERIALIZED_FILE = new File(gitlet.Gitlet.GITLET_DIR + ".blobs/");
}
