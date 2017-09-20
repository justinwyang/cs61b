import org.junit.Test;
import static org.junit.Assert.*;

/** Runs various tests for the MatrixUtils class
 *
 *  @author Justin Yang
 */

public class MatrixUtilsTest {
    @Test
    public void test() {
        double[][] em = scan("4x6.png.energyMatrix.correct");
        double[][] hAccumExpect =
                scan("4x6.png.horizontalAccumulation.correct");
        int[] hSeamExpect = new int[]{1, 2, 1, 0};
        double[][] vAccumExpect = scan("4x6.png.verticalAccumulation.correct");
        int[] vSeamExpect = new int[]{1, 2, 1, 1, 2, 1};

        check(hAccumExpect, MatrixUtils.accumulate(em,
                MatrixUtils.Orientation.HORIZONTAL));
        assertArrayEquals(hSeamExpect, MatrixUtils.findSeam(hAccumExpect,
                MatrixUtils.Orientation.HORIZONTAL));
        check(vAccumExpect, MatrixUtils.accumulateVertical(em));
        assertArrayEquals(vSeamExpect,
                MatrixUtils.findVerticalSeam(vAccumExpect));
    }

    public void check(double[][] expected, double[][] actual) {
        assert expected.length == actual.length;
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], EPSILON);
        }
    }

    public double[][] scan(String filename) {
        double[][] vals = new double[ROWS][COLS];
        String[] splitted = Utils.readFile(filename).split("\\s+");
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                vals[r][c] = Double.parseDouble(splitted[r * COLS + c + 1]);
            }
        }
        return vals;
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }

    private static final int ROWS = 6;
    private static final int COLS = 4;
    private static final double EPSILON = 0.00001;
}
