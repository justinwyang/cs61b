/** Provides a variety of utilities for operating on matrices.
 *  All methods assume that the double[][] arrays provided are rectangular.
 *
 *  @author Josh Hug and Justin Yang
 */

public class MatrixUtils {
    /** enum for specifying vertical vs. horizontal orientation
     *  You can use this, for example, by writing Orientation x = VERTICAL
     *
     *  If you're using this from another class, you'll need to use the class
     *  name as well, e.g.
     *  MatrixUtils.Orientation x = MatrixUtils.Orientation.VERTICAL
     *
     *  See http://goo.gl/73xqAu for more.
     */

    static enum Orientation { VERTICAL, HORIZONTAL };

    /** Non-destructively accumulates an energy matrix in the vertical
     *  direction.
     *
     *  Given an energy matrix M, returns a new matrix am[][] such that
     *  for each index i, j, the value in am[i][j] is the minimum total
     *  energy required to reach position i, j from any spot in the top
     *  row. See the bottom of this comment for an example.
     *
     *  Potentially useful methods: MatrixUtils.copy
     *
     *  A helper method you might consider writing:
     *    get(double[][] e, int r, int c): Returns the e[r][c] if
     *         r and c are valid. Double.POSITIVE_INFINITY otherwise
     *
     *  An example is shown below. See the assignment spec for a
     *  detailed explanation of this example.
     *
     *  Sample input:
     *  1000000   1000000   1000000   1000000
     *  1000000     75990     30003   1000000
     *  1000000     30002    103046   1000000
     *  1000000     29515     38273   1000000
     *  1000000     73403     35399   1000000
     *  1000000   1000000   1000000   1000000
     *
     *  Output for sample input:
     *  1000000   1000000   1000000   1000000
     *  2000000   1075990   1030003   2000000
     *  2075990   1060005   1133049   2030003
     *  2060005   1089520   1098278   2133049
     *  2089520   1162923   1124919   2098278
     *  2162923   2124919   2124919   2124919
     *
     */

    public static double[][] accumulateVertical(double[][] m) {
        double[][] am = new double[m.length][m[0].length];
        for (int i = 0; i < m[0].length; i++) {
            am[0][i] = m[0][i];
        }
        for (int i = 1; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                am[i][j] = m[i][j] + Math.min(get(m, i - 1, j),
                        Math.min(get(m, i - 1, j - 1), get(m, i - 1, j + 1)));
            }
        }
        return am;
    }

    /** Retrieves the value e[r][c] it it exists. If it doesn't exists,
     *  returns Double.POSITIVE_INFINITY
     *
     *  @param e the matrix to retrieve a value from
     *  @param r row
     *  @param c column
     */

    public static double get(double[][] e, int r, int c) {
        if (r >= 0 && r < e.length && c >= 0 && c < e[0].length) {
            return e[r][c];
        }
        return Double.POSITIVE_INFINITY;
    }

    /** Non-destructively accumulates a matrix M along the specified
     *  ORIENTATION.
     *
     *  If the orientation is Orientation.VERTICAL, function is identical to
     *  accumulateVertical. If Orientation.HORIZONTAL, function is
     *  the same, but with roles of r and c reversed.
     *
     *  Do NOT copy and paste a bunch of code! Instead, you should write
     *  a helper function that creates a new matrix mT that contains all
     *  the information from m, but with the property that
     *  accumulateVertical(mT) returns the correct result.
     *
     *  accumulate should be very short (only a few lines). Most of the
     *  work should be done in creaing the helper function (and even
     *  that function should be pretty short and straightforward).
     *
     *  The important lesson here is that you should never have big
     *  copy and pastes of any code. Instead, find the right
     *  abstraction that lets you avoid this mess. You'll need to do this
     *  for project 1, but in a more complex way.
     *
     */

    public static double[][] accumulate(double[][] m, Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            return accumulateVertical(getTranspose(m));
        }
        return accumulateVertical(m);
    }

    /** Returns the transpose of a matrix m.
     *
     *  @param m the matrix to find the transpose of
     */

    public static double[][] getTranspose(double[][] m) {
        double[][] mT = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                mT[j][i] = m[i][j];
            }
        }
        return mT;
    }

    /** Finds the vertical seam VERTSEAM of the given matrix M.
     *
     *  Potentially useful helper function: Something that takes
     *  an array, a lo index, and a hi index, and returns the
     *  index of the smallest thing in that array between those
     *  indices (inclusive).
     *
     *  Such a helper function will keep your code simple.
     *
     *  To keep the HW from getting too long, this method (and the
     *  next) is optional. however, completing it will allow you to
     *  see the resizing algorithm (that you wrote!) in action.
     *
     *  Sample input:
     *  1000000   1000000   1000000   1000000
     *  2000000   1075990   1030003   2000000
     *  2075990   1060005   1133049   2030003
     *  2060005   1089520   1098278   2133049
     *  2089520   1162923   1124919   2098278
     *  2162923   2124919   2124919   2124919
     *
     *  Output for sameple input: {1, 2, 1, 1, 2, 1}.
     *  See 4x6.png.verticalSeam.correct for a visual picture.
     *
     *  This answer is NOT unique. There are other correct seams.
     *  One way to test seam correctness is to check that the
     *  total energy is approximately equal.
     */

    public static int[] findVerticalSeam(double[][] m) {
        double[][] e = accumulateVertical(m);
        int[] seam = new int[m.length];
        seam[m.length - 1] = findSmallestIdx(e);
        for (int i = m.length - 1; i > 0; i--) {
            int curIdx = seam[i];
            double prevValue = e[i][curIdx] - m[i][curIdx];
            for (int d = -1; d <= 1; d++) {
                double toMatch = get(e, i - 1, curIdx + d);
                if (Math.abs(toMatch - prevValue) < EPSILON) {
                    seam[i - 1] = curIdx + d;
                }
            }
        }
        return seam;
    }

    /** Calculates the index of the minimum final energy value.
     *
     *  @param e the energy matrix
     *  @return the index of the minimum final energy
     */
    public static int findSmallestIdx(double[][] e) {
        double minEnergy = Double.POSITIVE_INFINITY;
        int idx = -1;
        for (int i = 0; i < e[e.length - 1].length; i++) {
            if (e[e.length - 1][i] < minEnergy) {
                minEnergy = e[e.length - 1][i];
                idx = i;
            }
        }
        return idx;
    }

    /** Returns the SEAM of M with the given ORIENTATION.
     *  As with accumulate, this should be really short and use
     *  a helper method.
     */

    public static int[] findSeam(double[][] m, Orientation orientation) {
        if (orientation == Orientation.HORIZONTAL) {
            return findVerticalSeam(getTranspose(m));
        }
        return findVerticalSeam(m);
    }

    /** does nothing. ARGS not used. use for whatever purposes you'd like */
    public static void main(String[] args) {
        /* sample calls to functions in this class are below

        ImageRescaler sc = new ImageRescaler("4x6.png");

        double[][] em = ImageRescaler.energyMatrix(sc);

        //double[][] m = sc.cumulativeEnergyMatrix(true);
        double[][] m = MatrixUtils.accumulateVertical(em);
        System.out.println(MatrixUtils.matrixToString(em));
        System.out.println();

        double[][] ms = MatrixUtils.accumulate(em, Orientation.HORIZONTAL);

        System.out.println(MatrixUtils.matrixToString(m));
        System.out.println();
        System.out.println(MatrixUtils.matrixToString(ms));


        int[] lep = MatrixUtils.findVerticalSeam(m);

        System.out.println(seamToString(m, lep, Orientation.VERTICAL));

        int[] leps = MatrixUtils.findSeam(ms, Orientation.HORIZONTAL);

        System.out.println(seamToString(ms, leps, Orientation.HORIZONTAL));
        */
    }


    /** Below follow some utility functions. Also see Utils.java. */

    /** Returns a string representaiton of the given matrix M.
     */

    public static String matrixToString(double[][] m) {
        int height = m.length;
        int width = m[0].length;
        StringBuilder sb = new StringBuilder();

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                sb.append(String.format("%9.0f ", m[r][c]));
            }

            sb.append("\n");
        }
        return sb.toString();
    }

    /** Returns a copy of the matrix M. Note that it is probably a better idea
     *  to use System.arraycopy for general 2D arrays since
     */

    public static double[][] copy(double[][] m) {
        int height = m.length;
        int width = m[0].length;

        double[][] copy = new double[height][];
        for (int r = 0; r < height; r++) {
            copy[r] = new double[width];
            double[] thisRow = m[r];
            System.arraycopy(thisRow, 0, copy[r], 0, width);
        }
        return copy;
    }

    /** Checks to see if a given SEAM is valid given a particular
     *  image HEIGHT, WIDTH, and a seam ORIENTATION. Throws an illegal
     *  argument exception if invalid.
     */

    public static void validateSeam(int height, int width,
                           int[] seam, Orientation orientation) {

        if ((orientation == Orientation.VERTICAL)
             && (seam.length != height)) {
            throw new IllegalArgumentException("Bad vertical seam length.");
        }

        if ((orientation == Orientation.HORIZONTAL)
            && (seam.length != width)) {
            throw new IllegalArgumentException("Bad horizontal seam length.");
        }

        int maxValue;
        if (orientation == Orientation.VERTICAL) {
            maxValue = width - 1;
        } else {
            maxValue = height - 1;
        }


        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= maxValue) {
                String msg = "Seam value out of bounds.";
                throw new IllegalArgumentException(msg);
            }
        }

        for (int i = 0; i < seam.length - 1; i++) {
            int difference = Math.abs(seam[i] - seam[i + 1]);


            if (difference > 1) {
                String msg = "Seam not contiguous.";
                throw new IllegalArgumentException(msg);
            }
        }
    }


    /** Returns a string representation of  the matrix M annotated with the
     *  provided SEAM along the ORIENTATION specified.
     */

    public static String seamToString(double[][] m, int[] seam,
                                     Orientation orientation) {

        StringBuilder sb = new StringBuilder();
        int height = m.length;
        int width = m[0].length;

        validateSeam(height, width, seam, orientation);

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                char lMarker = ' ';
                char rMarker = ' ';
                if (orientation == Orientation.VERTICAL) {
                    if (c == seam[r]) {
                        lMarker = '[';
                        rMarker = ']';
                    }
                } else {
                    if (r == seam[c]) {
                        lMarker = '[';
                        rMarker = ']';
                    }
                }
                sb.append(String.format("%c%6.0f%c ",
                          lMarker, m[r][c], rMarker));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /** The value epsilon, to be used in double comparisons. */

    private static final double EPSILON = 0.00001;

}
