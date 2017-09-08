import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 *  @author Justin Yang
 */
public class ListTour {
    /** Print the items in L, then print the list backwards (ultimately
      * including the last item twice). You should only create one
      * iterator. Use for loops for best style.
      *
      * As an example, suppose the list contains "alfred", "bob"
      * "glorgus", and "stipstip", you should get:
      *
      * alfred bob glorgus stipstip stipstip glorgus bob alfred
      */
    static void printTour(List<String> L) {
        ListIterator<String> p = L.listIterator();
        for (; p.hasNext(); ) {
            System.out.println(p.next());
        }
        for (; p.hasPrevious(); ) {
            System.out.println(p.previous());
        }
    }

    /** Quick and dirty main method that calls static methods
        above. ARGS is unused. */
    public static void main(String[] args) {
        ArrayList<String> L = new ArrayList<String>();
        L.add("I");
        L.add("am");
        L.add("Justin");
        L.add("Yang");
        printTour(L);
    }

}

