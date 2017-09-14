import org.junit.Test;
import static org.junit.Assert.*;

/** Tests various cases for the Lists problem.
 *
 *  @author Justin Yang
 */

public class ListsTest {
    @Test
    public void test() {
        IntList a = IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11);
        IntList2 a1 = IntList2.list(new int[][]{{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}});
        assertEquals("Normal Case", a1, Lists.naturalRuns(a));

        /*
        assertEquals(".size() after remove(0)",4, d.size());
        assertEquals(".get(0)", 2, d.get(0));
        assertEquals(".get(1)", 3, d.get(1));
        assertEquals(".get(2)", 2, d.get(2));
        assertEquals(".get(3)", 1, d.get(3));

        d.remove(3);
        assertEquals(".size() after remove(3)",3, d.size());
        assertEquals(".get(0)", 2, d.get(0));
        assertEquals(".get(1)", 3, d.get(1));
        assertEquals(".get(2)", 2, d.get(2));

        d.remove(-3);
        assertEquals(".size() after remove(-3)",2, d.size());
        assertEquals(".get(0)", 3, d.get(0));
        assertEquals(".get(1)", 2, d.get(1));

        d.remove(-1);
        assertEquals(".size() after remove(-1)",1, d.size());
        assertEquals(".get(0)", 3, d.get(0));
        */
    }

    // It might initially seem daunting to try to set up
    // Intlist2 expected.
    //
    // There is an easy way to get the IntList2 that you want in just
    // few lines of code! Make note of the IntList2.list method that
    // takes as input a 2D array.

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
