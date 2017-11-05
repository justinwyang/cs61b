import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/** A set of String values.
 *  @author Justin Yang
 */
class ECHashStringSet implements StringSet {

    /** Constructor for ECHashStringSet. */
    public ECHashStringSet() {
        _buckets = new StringList[1];
        _size = 0;
    }

    @Override
    public boolean contains(String s) {
        return _buckets[bucketIdx(s)] != null
                && _buckets[bucketIdx(s)].contains(s);
    }

    @Override
    public void put(String s) {
        if (contains(s)) {
            return;
        }
        if ((double) _size / bucketCount() > MAX_LOAD_FACTOR) {
            reallocate();
        }
        insert(s);
        _size++;
    }

    /** Reallocates the array. */
    private void reallocate() {
        StringList[] old = _buckets;
        _buckets = new StringList[bucketCount() * 2];
        for (StringList bucket: old) {
            if (bucket != null) {
                for (String s: bucket) {
                    insert(s);
                }
            }
        }
    }

    /** Inserts the String s into the array.
     *
     * @param s the string to insert
     */
    private void insert(String s) {
        int idx = bucketIdx(s);
        if (_buckets[idx] == null) {
            _buckets[idx] = new StringList();
        }
        _buckets[idx].add(s);
    }

    @Override
    public List<String> asList() {
        StringList list = new StringList();
        for (StringList bucket: _buckets) {
            if (bucket != null) {
                for (String s: bucket) {
                    list.add(s);
                }
            }
        }
        Collections.sort(list);
        return list;
    }

    /** Returns the bucket count.
     *
     * @return bucket count
     */
    private int bucketCount() {
        return _buckets.length;
    }

    /** Returns the bucket index of a string.
     *
     * @param s the string passed in
     * @return the bucket index
     */
    private int bucketIdx(String s) {
        return (s.hashCode() & Integer.MAX_VALUE) % bucketCount();
    }

    /** Allows initialization of ArrayList<String> array. */
    private static class StringList extends ArrayList<String> {
    }

    /** Stores the values in a hash array. */
    private StringList[] _buckets;

    /** The number of strings stored. */
    private int _size;

    /** The maximum load factor. */
    private static final int MAX_LOAD_FACTOR = 5;
}
