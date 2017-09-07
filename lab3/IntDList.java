
public class IntDList {

    protected DNode _front, _back;

    public IntDList() {
        _front = _back = null;
    }

    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /** Returns the first item in the IntDList. */
    public int getFront() {
        return _front._val;
    }

    /** Returns the last item in the IntDList. */
    public int getBack() {
        return _back._val;
    }

    /** Return value #I in this list, where item 0 is the first, 1 is the
     *  second, ...., -1 is the last, -2 the second to last.... */
    public int get(int i) {
        if (i < 0) {
            i = i + size();
        }

        DNode cur;
        cur = _front;
        for (int j = 0; j < i; j++) {
            cur = cur._next;
        }

        return cur._val;
    }

    /** The length of this list. */
    public int size() {
        DNode cur = _front;
        int count = 0;
        while (cur != null) {
            cur = cur._next;
            count++;
        }
        return count;
    }

    /** Adds D to the front of the IntDList. */
    public void insertFront(int d) {
        if (_front == null) { /* Empty IntDList */
            _front = _back = new DNode(null, d, null);
        } else {
            _front = new DNode(null, d, _front); /* set the back node*/
            _front._next._prev = _front;
        }
    }

    /** Adds D to the back of the IntDList. */
    public void insertBack(int d) {
        if (_front == null) { /* Empty IntDList */
            _front = _back = new DNode(null, d, null);
        } else {
            _back = new DNode(_back, d, null); /* set the back node*/
            _back._prev._next = _back;
        }
    }

    /** Removes the last item in the IntDList and returns it.
     * This is an extra challenge problem. */
    public int deleteBack() {
        int val = _back._val;
        if (_front == _back) {
            _front = _back = null;
        } else {
            _back = _back._prev;
            _back._next = null;
        }
        return val;
    }

    /** Returns a string representation of the IntDList in the form
     *  [] (empty list) or [1, 2], etc. 
     * This is an extra challenge problem. */
    public String toString() {
        if (size() == 0) {
            return "[]";
        }
        String string = "[";
        DNode cur = _front;
        while (cur != null) {
            string += cur._val;
            if (cur != _back) {
                string += ", ";
            }
            cur = cur._next;
        }
        return string + "]";
    }

    /* DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information! */
    protected static class DNode {
        protected DNode _prev;
        protected DNode _next;
        protected int _val;

        private DNode(int val) {
            this(null, val, null);
        }

        private DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
