import java.util.Iterator;
import utils.Filter;

/** A kind of Filter that lets through every other VALUE element of
 *  its input sequence, starting with the first.
 *  @author Justin Yang
 */
class AlternatingFilter<Value> extends Filter<Value> {

    /** A filter of values from INPUT that lets through every other
     *  value. */
    AlternatingFilter(Iterator<Value> input) {
        super(input);
        _valid = false;
    }

    @Override
    protected boolean keep() {
        _valid = !_valid;
        return _valid;
    }

    /** Stores whether the previous value was valid.
     */
    private boolean _valid;

}
