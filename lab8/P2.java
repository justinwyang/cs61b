import java.util.Scanner;

/** Solved problem 2, lab8.
 *  @author Justin Yang
 */
public class P2 {

    /** Processes the preorder and inorder Strings to calculate inorder.
     *
     * @param pre preorder
     * @param in inorder
     * @return postorder
     */
    public static String process(String pre, String in) {
        if (pre.length() <= 1) {
            return pre;
        }

        char node = pre.charAt(0);
        String post = "";
        int idx = -1;
        for (int i = 0; i < in.length(); i++) {
            if (in.charAt(i) == node) {
                idx = i;
                break;
            }
        }

        post += process(pre.substring(1, idx + 1),
                in.substring(0, idx));

        if (idx < pre.length() - 1) {
            post += process(pre.substring(idx + 1),
                    in.substring(idx + 1));
        }

        post += node;

        return post;
    }

    /** The main method.
     */
    public static void main(String... ignored) {
        Scanner scan = new Scanner(System.in);

        for (int i = 1; scan.hasNext(); i++) {
            String pre = scan.next();
            String in = scan.next();
            String post = process(pre, in);
            System.out.println("Case " + i + ": " + post + "\n");
        }

        scan.close();
    }

}
