/* NOTE: The file ArrayUtil.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author Justin Yang
 */
class Arrays {
    /* C. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        int[] cat = new int[A.length + B.length];
        System.arraycopy(A, 0, cat, 0, A.length);
        System.arraycopy(B, 0, cat, A.length, B.length);
        return cat;
    }

    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        int[] rem = new int[A.length - len];
        System.arraycopy(A, 0, rem, 0, start);
        System.arraycopy(A, start + len, rem, start, A.length - (start + len));
        return rem;
    }

    /* E. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        int[][] runs = new int[A.length][];
        int start = 0, cur = 0;
        for (int i = 0; i < A.length; i++) {
            if (i == A.length - 1 || A[i] >= A[i + 1]) {
                runs[cur] = Utils.subarray(A, start, i + 1 - start);
                cur++;
                start = i + 1;
            }
        }
        int[][] result = new int[cur][];
        System.arraycopy(runs, 0, result, 0, cur);
        return result;
    }
}
