import java.util.Scanner;

/** Solved problem 3, lab8.
 *  @author Justin Yang
 */
public class P3 {

    /** Checks if a given string and appending character is valid.
     *
     * @param s The current String.
     * @param c The character to append.
     * @return  Whether the pair is valid or not.
     */
    public static boolean valid(String s, char c) {
        s += c;
        if (s.length() < 2) {
            return true;
        }
        for (int i = 1; i <= s.length() / 2; i++) {
            int a = s.length() - i;
            int b = a - i;
            if (s.substring(a).equals(s.substring(b, a))) {
                return false;
            }
        }
        return true;
    }

    /** Relaxes the String with greater values.
     *
     * @param s The String to be relaxed.
     * @return  The relaxed String.
     */
    public static String relax(String s) {
        if (s.equals("")) {
            return s;
        }
        String split = s.substring(0, s.length() - 1);
        char last = s.charAt(s.length() - 1);

        do {
            if (last == '3') {
                split = relax(split);
                last = '1';
            } else {
                last += 1;
            }
        } while (!valid(split, last));
        return split + last;
    }

    /** Finds the minimal answer for a given length.
     *
     * @param length The length requested.
     * @return  The generated String.
     */
    public static String process(int length) {
        if (length == 0) {
            return "";
        }
        String value = process(length - 1);
        char next = '1';
        while (!valid(value, next)) {
            boolean works = false;
            for (; next <= '3'; next++) {
                if (valid(value, next)) {
                    works = true;
                    break;
                }
            }
            if (works) {
                break;
            } else {
                value = relax(value);
                next = '1';
            }
        }
        return value + next;
    }

    /** The main method.
     */
    public static void main(String... ignored) {
        Scanner scan = new Scanner(System.in);

        while (scan.hasNext()) {
            int length = scan.nextInt();
            String printout = "The smallest good numeral of length "
                    + length + " is " + process(length) + ".";
            System.out.println(printout);
        }

        scan.close();
    }


}
