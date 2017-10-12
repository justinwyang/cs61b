import java.util.Scanner;

/** Solved problem 1, lab8.
 *  @author Justin Yang
 */
public class P1 {

    /** Processes input to calculate the blank space.
     *
     * @param scan The given Scanner.
     * @return  The calculated value.
     */
    public static int process(Scanner scan) {
        int minBlanks = Integer.MAX_VALUE, numRows = 0, sum = 0;
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            if (line.equals("")) {
                break;
            }

            int blanks = 0;
            boolean start = true;
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == ' ') {
                    blanks++;
                    if (start) {
                        start = false;
                    }
                }
                if (line.charAt(i) == 'X' && !start) {
                    break;
                }
            }
            minBlanks = Math.min(minBlanks, blanks);
            numRows++;
            sum += blanks;
        }

        sum -= minBlanks * numRows;
        return sum;
    }

    /** The main method.
     */
    public static void main(String... ignored) {
        Scanner scan = new Scanner(System.in);

        for (int i = 1; scan.hasNextLine(); i++) {
            int ans = process(scan);
            System.out.println("Image " + i + ": " + ans + "\n");
        }

        scan.close();
    }


}
