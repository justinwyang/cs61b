/**
 * Created by joshuazeitsoff on 9/2/17.
 */

public class BuggyIntDListSolution extends IntDList {

    public BuggyIntDListSolution(Integer... values) {
        super(values);
    }

    /** Removes value #I in this list, where item 0 is the first, 1 is the
     *  second, ...., -1 is the last, -2 the second to last.... */
    public void remove(int i) {
        if (_front == _back) {
            _front = _back = null;
            return;
        }
        if (i == 0 || i == -size()) {
            _front = _front._next;
            _front._prev = null;
            return;
        }
        DNode p;
        if (i >= 0) {
            p = _front;
            while (i > 0) {
                i -= 1;
                p = p._next;
            }
        } else {
            p = _back;
            while (i < -1) {
                i += 1;
                p = p._prev;
            }
        }
        if (p._next != null) {
            p._next._prev = p._prev;
        } else {
            _back = p._prev;
        }
        p._prev._next = p._next;
    }
    public String getException() {
        //hint : this is what comes after the "java.lang" at the top of the stack trace
        return "NullPointerException";
    }

    public String getErrorFunction() {
        // hint: this is the first function name that you see
        // when reading the stack trace from top to bottom
        return "BuggyIntDList.remove()";
    }

    public int getErrorLineNumber() {
        // PUT ERROR LINE NUMBER HERE
        // hint: this is the number that comes after whichever function the
        // error is occurring in.
        return 33;
    }
}
