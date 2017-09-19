import org.junit.Test;
import static org.junit.Assert.*;

/** Runs various tests for the Arrays class.
 *
 *  @author Justin Yang
 */

public class ArraysTest {
    @Test
    public void testCatenate() {
        int[] a, b, ab;

        a = new int[]{1, 2, 3, 4, 5};
        b = new int[]{3, 2, 4};
        ab = new int[]{1, 2, 3, 4, 5, 3, 2, 4};
        assertArrayEquals("Normal Case", ab, Arrays.catenate(a, b));

        a = new int[]{};
        b = new int[]{3, 2, 4};
        ab = new int[]{3, 2, 4};
        assertArrayEquals("Empty A", ab, Arrays.catenate(a, b));

        a = new int[]{1, 2, 3, 4, 5};
        b = new int[]{};
        ab = new int[]{1, 2, 3, 4, 5};
        assertArrayEquals("Empty B", ab, Arrays.catenate(a, b));

        a = new int[]{};
        b = new int[]{};
        ab = new int[]{};
        assertArrayEquals("Both Empty", ab, Arrays.catenate(a, b));
    }

    @Test
    public void testRemove() {
        int[] a, rem;
        int start, len;

        a = new int[]{1, 2, 3, 4, 5};
        start = 1;
        len = 2;
        rem = new int[]{1, 4, 5};
        assertArrayEquals("Normal Case", rem, Arrays.remove(a, start, len));

        a = new int[]{1, 2, 3, 4, 5};
        start = 4;
        len = 0;
        rem = new int[]{1, 2, 3, 4, 5};
        assertArrayEquals("Trailing Length 0", rem,
                Arrays.remove(a, start, len));

        a = new int[]{1, 2, 3, 4, 5};
        start = 0;
        len = 0;
        rem = new int[]{1, 2, 3, 4, 5};
        assertArrayEquals("Heading Length 0", rem,
                Arrays.remove(a, start, len));

        a = new int[]{1, 2, 3, 4, 5};
        start = 0;
        len = 5;
        rem = new int[]{};
        assertArrayEquals("Remove All", rem, Arrays.remove(a, start, len));

        a = new int[]{};
        start = 0;
        len = 0;
        rem = new int[]{};
        assertArrayEquals("Empty Starter", rem, Arrays.remove(a, start, len));
    }

    @Test
    public void testNaturalRuns() {
        int[] a;
        int[][] a1;

        a = new int[]{1, 3, 7, 5, 4, 6, 9, 10, 10, 11};
        a1 = new int[][]{{1, 3, 7}, {5}, {4, 6, 9, 10}, {10, 11}};
        assertArrayEquals("Normal Case", a1, Arrays.naturalRuns(a));

        a = new int[]{5, 4, 3, 2, 1};
        a1 = new int[][]{{5}, {4}, {3}, {2}, {1}};
        assertArrayEquals("All Decreasing", a1, Arrays.naturalRuns(a));

        a = new int[]{1, 2, 3, 4, 5};
        a1 = new int[][]{{1, 2, 3, 4, 5}};
        assertArrayEquals("All Increasing", a1, Arrays.naturalRuns(a));

        a = new int[]{};
        a1 = new int[][]{};
        assertArrayEquals("Empty", a1, Arrays.naturalRuns(a));
    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
