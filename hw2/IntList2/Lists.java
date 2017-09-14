/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

import java.util.Iterator;


/** List problem.
 *  @author Justin Yang
 */
class Lists {
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    static IntList2 naturalRuns(IntList L) {
        IntList2 list2 = null, cur = null;
        IntList start = L;

        while (L != null) {
            if (L.tail != null && L.head > L.tail.head) {
                IntList2 next = new IntList2(start, null);
                if (list2 == null) {
                    cur = list2 = next;
                } else {
                    cur.tail = next;
                    cur = cur.tail;
                }
                start = L.tail;
                L.tail = null;
                L = start;
            } else {
                L = L.tail;
            }
        }
        return list2;
    }
}
