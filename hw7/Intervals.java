import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/** HW #8, Problem 3.
 *  @author Justin Yang
  */
public class Intervals {
    /** Assuming that INTERVALS contains two-element arrays of integers,
     *  <x,y> with x <= y, representing intervals of ints, this returns the
     *  total length covered by the union of the intervals. */
    public static int coveredLength(List<int[]> intervals) {
        intervals.sort(new Compare());

        int len = 0;
        int[] prev = {0, 0};
        for (int[] cur: intervals) {
            if (prev != null) {
                if (cur[0] <= prev[1]) {
                    prev = new int[] {prev[0], Math.max(prev[1], cur[1])};
                } else {
                    len += prev[1] - prev[0];
                    prev = cur;
                }
            } else {
                prev = cur;
            }
        }
        len += prev[1] - prev[0];

        return len;
    }

    /** Compare class, used as a Comparator to sort intervals. */
    static class Compare implements Comparator<int[]> {
        @Override
        public int compare(int[] a, int[] b) {
            return a[0] - b[0];
        }
    }

    /** Test intervals. */
    static final int[][] INTERVALS = {
        {19, 30},  {8, 15}, {3, 10}, {6, 12}, {4, 5},
    };
    /** Covered length of INTERVALS. */
    static final int CORRECT = 23;

    /** Performs a basic functionality test on the coveredLength method. */
    @Test
    public void basicTest() {
        assertEquals(CORRECT, coveredLength(Arrays.asList(INTERVALS)));
    }

    /** Runs provided JUnit test. ARGS is ignored. */
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Intervals.class));
    }

}
