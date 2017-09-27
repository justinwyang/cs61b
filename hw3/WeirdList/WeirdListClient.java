/** Functions to increment and sum the elements of a WeirdList.
 * @author Justin Yang
 */
class WeirdListClient {

    /** Return the result of adding N to each element of L. */
    static WeirdList add(WeirdList L, int n) {
        return L.map(new Adder(n));
    }

    /** Return the sum of the elements in L. */
    static int sum(WeirdList L) {
        CumulativeAdder accumulate = new CumulativeAdder();
        L.map(accumulate);
        return accumulate.getValue();
    }

    /** Class intended to add values. */
    private static class Adder implements IntUnaryFunction {
        /** Constructor for the Adder class.
         *  @param n the value to add
         */
        public Adder(int n) {
            _n = n;
        }

        /** Returns the adder value. */
        public int getValue() {
            return _n;
        }

        @Override
        public int apply(int x) {
            return _n + x;
        }

        /** The current adder value. */
        protected int _n;
    }

    /** Subclass of Adder meant to accumulate values passed in. */
    private static class CumulativeAdder extends Adder {
        /** Constructor for CumulativeAdder. */
        public CumulativeAdder() {
            super(0);
        }

        @Override
        public int apply(int x) {
            _n = super.apply(x);
            return _n;
        }
    }

    /* As with WeirdList, you'll need to add an additional class or
     * perhaps more for WeirdListClient to work. Again, you may put
     * those classes either inside WeirdListClient as private static
     * classes, or in their own separate files.

     * You are still forbidden to use any of the following:
     *       if, switch, while, for, do, try, or the ?: operator.
     */
}
