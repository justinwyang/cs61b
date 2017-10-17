/** Represents an array of integers each in the range -8..7.
 *  Such integers may be represented in 4 bits (called nybbles).
 *  @author Justin Yang
 */
public class Nybbles {

    /** Maximum positive value of a Nybble. */
    public static final int MAX_VALUE = 7;

    /** Return an array of size N. */
    public Nybbles(int N) {
        _data = new int[(N + 7) / 8];
        _n = N;
    }

    /** Return the size of THIS. */
    public int size() {
        return _n;
    }

    /** Return the Kth integer in THIS array, numbering from 0.
     *  Assumes 0 <= K < N. */
    public int get(int k) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else {
            int idx = k / 8;
            int pos = k % 8;
            int bits = 4;
            int shift = 32 - bits;
            int val = (_data[idx] << (shift - pos * bits)) >>> shift;
            val -= (val / 8) * 16;
            return val;
        }
    }

    /** Set the Kth integer in THIS array to VAL.  Assumes
     *  0 <= K < N and -8 <= VAL < 8. */
    public void set(int k, int val) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else if (val < (-MAX_VALUE - 1) || val > MAX_VALUE) {
            throw new IllegalArgumentException();
        } else {
            int idx = k / 8;
            int pos = k % 8;
            int offset = 16;
            int bits = 4;
            int shift = 32 - bits;
            if (val < 0) {
                val += offset;
            }
            _data[idx] -= (_data[idx] << (shift - pos * bits)) >>> shift;
            _data[idx] += val << (pos * 4);
        }
    }

    /** Size of current array (in nybbles). */
    private int _n;
    /** The array data, packed 8 nybbles to an int. */
    private int[] _data;
}
