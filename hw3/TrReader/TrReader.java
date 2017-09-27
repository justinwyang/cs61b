import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Justin Yang
 */
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(0) to TO.charAt(0), etc., leaving other characters
     *  unchanged.  FROM and TO must have the same length. */
    public TrReader(Reader str, String from, String to) {
        _reader = str;
        _from = from;
        _to = to;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int ret = _reader.read(cbuf, off, len);
        for (int i = 0; i < cbuf.length; i++) {
            cbuf[i] = convert(cbuf[i]);
        }
        return ret;
    }

    /** Returns a converted char, otherwise returns without changes.
     * @param c the char to convert
     */
    public char convert(char c) {
        for (int i = 0; i < _from.length(); i++) {
            if (_from.charAt(i) == c) {
                return _to.charAt(i);
            }
        }
        return c;
    }

    @Override
    public void close() throws IOException {
        _reader.close();
    }

    /** The Reader to read in a buffer from. */
    private Reader _reader;

    /** The "from" String for conversion. */
    private String _from;

    /** The "to" String for conversion. */
    private String _to;
}


