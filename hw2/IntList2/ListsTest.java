import org.junit.Test;
import static org.junit.Assert.*;

/** Runs various tests for the Lists class.
 *
 *  @author Justin Yang
 */

public class ListsTest {
    @Test
    public void test() {
        IntList a;
        IntList2 a1;

        a = IntList.list(1, 3, 7, 5, 4, 6, 9, 10, 10, 11);
        a1 = IntList2.list(
            new int[][]{{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}});
        assertEquals("Normal Case", a1, Lists.naturalRuns(a));

        a = IntList.list(5, 4, 3, 2, 1);
        a1 = IntList2.list(new int[][]{{5}, {4}, {3}, {2}, {1}});
        assertEquals("All Decreasing", a1, Lists.naturalRuns(a));

        a = IntList.list(1, 2, 3, 4, 5);
        a1 = IntList2.list(new int[][]{{1, 2, 3, 4, 5}});
        assertEquals("All Increasing", a1, Lists.naturalRuns(a));

        a = IntList.list();
        a1 = IntList2.list(new int[][]{});
        assertEquals("Empty", a1, Lists.naturalRuns(a));
    }

    /*
     It might initially seem daunting to try to set up
     Intlist2 expected.

     There is an easy way to get the IntList2 that you want in just
     few lines of code! Make note of the IntList2.list method that
     takes as input a 2D array.
    */

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
