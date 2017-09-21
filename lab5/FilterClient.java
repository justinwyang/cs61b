import utils.Filter;
import utils.Predicate;
import java.util.List;
import java.util.Arrays;
import java.util.Iterator;

/** Exercises for Lab 6.
 *  @author Justin Yang
 */
public class FilterClient {

    /** A couple of test cases. */
    private static final Integer[][] TESTS = {
        { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 },
        { 1, 2, 3, 0, 7, 8, 6, 9, 10, 1 }
    };

    /** Print out the items returned by L. */
    static void printAll(Filter<Integer> L) {
        System.out.print("[");
        String sep;
        sep = "";
        for (Integer i : L) {
            System.out.print(sep + i);
            sep = ", ";
        }
        System.out.println("]");
    }

    /** A sample space where you can experiment with your filter.
      * ARGS is unused. */
    public static void main(String[] args) {
        for (Integer[] data: TESTS) {
            List<Integer> L = Arrays.asList(data);
            System.out.println(L);
            System.out.println();

            Filter<Integer> f1 = new TrivialFilter<Integer>(L.iterator());
            System.out.println("Trivial Filter:");
            printAll(f1);

            Filter<Integer> f2 = new AlternatingFilter<>(L.iterator());
            System.out.println("Alternating Filter:");
            printAll(f2);

            Filter<Integer> f3 = everyFourth(L.iterator());
            System.out.println("Every Fourth:");
            printAll(f3);

            Filter<Integer> f4 = evenNumberFilter(L.iterator());
            System.out.println("Even Number Filter:");
            printAll(f4);

            System.out.println();

        }
    }

    /* Extra Challenges that you should complete without creating
       any new Filter implementations (i.e. you can create them
       using Trivial, Alternating, Monotonic, and/or PredicateFilter)
       1. Create a filter everyFourth that prints very fourth
       item.
       2. Create a filter that prints only even valued items. You
       may find the Even class provided below to be helpful. */

    /** Returns a filter that delivers every fourth item of INPUT,
     *  starting with the first.  You should not need to define a new
     *  class. */
    static Filter<Integer> everyFourth(Iterator<Integer> input) {
        return new AlternatingFilter<>(new AlternatingFilter<>(input));
    }

    /** Returns a filter that delivers every even valued integer of
     *  INPUT. You should not need to define a new class. */
    static Filter<Integer> evenNumberFilter(Iterator<Integer> input) {
        return new PredicateFilter<>(new Even(), input);
    }

    /** A class whose instances represent the test for eveness. */
    static class Even implements Predicate<Integer> {
        @Override
        public boolean test(Integer x) {
            return x.intValue() % 2 == 0;
        }
    }
}
