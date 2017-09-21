import java.util.Set;
import java.util.TreeSet;

/** Contains code to Demo features of a Set.
 *
 *  @author Justin Yang
 */

public class SetDemo {

    /** Demos features of a Set.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        Set<String> set = new TreeSet<String>();

        set.add("papa");
        set.add("bear");
        set.add("mama");
        set.add("bear");
        set.add("baby");
        set.add("bear");

        System.out.println(set);
    }
}
