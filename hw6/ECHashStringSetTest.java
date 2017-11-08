import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.TreeSet;

/** Tests of EXHashStringSet.
 *  @author Justin Yang
 */
public class ECHashStringSetTest {

    @Test
    public void test() {
        ECHashStringSet set = new ECHashStringSet();
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

    @Test
    public void testTime() {
        TreeSet<String> tree = new TreeSet<String>();
        ECHashStringSet hash = new ECHashStringSet();
        String alphabet =
                "abcdefghijklmnopqrstuvqxyz";
        for (int i = 0; i < 1000; i++) {
            String random =
                    generateString(alphabet, (int) (Math.random() * 20));
            tree.add(random);
            hash.put(random);
        }

        for (String s: tree) {
            assertTrue("Strings must match!", hash.contains(s));
        }
    }

    /** Generates random string.
     *
     * @param characters the alphabetic characters
     * @param length length of the string.
     * @return random string
     */
    public static String generateString(String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.
                    charAt((int) (Math.random() * characters.length()));
        }
        return new String(text);
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ECHashStringSetTest.class));
    }
}
