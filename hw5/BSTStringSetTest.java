import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/** Tests of BSTStringSet.
 *  @author Justin Yang
 */
public class BSTStringSetTest {

    @Test
    public void test() {
        BSTStringSet set = new BSTStringSet();
        set.put("Ca");
        set.put("b");
        set.put("Z");
        set.put("X");
        set.put("L");
        set.put("N");

        List<String> list = set.asList();
        String[] strings = list.toArray(new String[list.size()]);

        assertArrayEquals(strings, new String[]{"Ca", "L", "N", "X", "Z", "b"});
        assertEquals("m is not in the list", set.contains("m"), false);
        assertEquals("Ca is not in the list", set.contains("Ca"), true);
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(BSTStringSetTest.class));
    }
}
