package gitlet;

import static gitlet.Utils.error;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *
 *  Acknowledgements: I discussed and worked on this project with
 *  Allen Chen (SID: 3032657006).
 *
 *  @author Justin Yang
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        try {
            if (args.length <= 0) {
                throw error("Please enter a command.");
            }

            String[] operands = new String[args.length - 1];
            for (int i = 1; i < args.length; i++) {
                operands[i - 1] = args[i];
            }

            new Gitlet().process(args[0], operands);

        } catch (GitletException error) {
            System.out.println(error.getMessage());
        }
    }

}
