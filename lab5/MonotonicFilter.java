import java.util.Iterator;
import utils.Filter;

/** A kind of Filter that lets all the VALUE elements of its input sequence
 *  that are larger than all the preceding values to go through the
 *  Filter.  So, if its input delivers (1, 2, 3, 3, 2, 1, 5), then it
 *  will produce (1, 2, 3, 5).
 *  @author Justin Yang
 */
class MonotonicFilter<Value extends Comparable<Value>> extends Filter<Value> {

    /** A filter of values from INPUT that delivers a monotonic
     *  subsequence.  */
    MonotonicFilter(Iterator<Value> input) {
        super(input);
        _max = null;
    }

    @Override
    protected boolean keep() {
        if (_max != null && _next.compareTo(_max) <= 0) {
            return false;
        }
        _max = _next;
        return true;
    }

    /** Stores the "maximum" value so far.
     */
    private Value _max;

}
