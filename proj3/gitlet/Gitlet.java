package gitlet;

import static gitlet.Utils.error;
import java.io.File;
import java.util.HashMap;
import java.util.function.Consumer;

/** Controls the main processes of Gitlet.
 *
 *  Acknowledgements: Much of the class structure, including
 *  the HashMap of commands, was borrowed from Game.java from
 *  Qirkat.
 *
 *  @author Justin Yang
 */
public class Gitlet {

    /** Processes and executes a Gitlet command.
     *
     * @param command the commands to process and execute.
     * @param operands the included operands
     */
    public void process(String command, String[] operands) {
        if (!_commands.containsKey(command)) {
            throw error("No command with that name exists.");
        }
        File gitletDir = new File(GITLET_DIR);
        if (command.equals("init") && gitletDir.exists()) {
            throw error
                    ("A Gitlet version-control system already exists in the current directory.");
        }
        if (!command.equals("init") && !gitletDir.exists()) {
            throw error
                    ("Not in an initialized Gitlet directory.");
        }
        _commands.get(command).accept(operands);
    }

    /** Checks for the correct number of operands.
     *
     * @param num the required number of operands
     */
    public void checkOperands(String[] operands, int num) {
        if (num != operands.length) {
            throw error("Incorrect operands.");
        }
    }

    /** Executes an init statement.
     *
     * @param operands operands for init (Should be none)
     */
    public void init(String[] operands) {
        File gitletDir = new File(GITLET_DIR);
        checkOperands(operands, 0);
        gitletDir.mkdir();

    }

    public void add(String[] operands) {

    }

    public void commit(String[] operands) {

    }

    public void rm(String[] operands) {

    }

    public void log(String[] operands) {

    }

    public void globalLog(String[] operands) {

    }

    public void find(String[] operands) {

    }

    public void status(String[] operands) {

    }

    public void checkout(String[] operands) {

    }

    public void branch(String[] operands) {

    }

    public void rmBranch(String[] operands) {

    }

    public void reset(String[] operands) {

    }

    public void merge(String[] operands) {

    }

    /** Mapping of commands to methods that process them. */
    private final HashMap<String, Consumer<String[]>> _commands =
            new HashMap<>(); {
        _commands.put("init", this::init);
        _commands.put("add", this::add);
        _commands.put("commit", this::commit);
        _commands.put("rm", this::rm);
        _commands.put("log", this::log);
        _commands.put("global-log", this::globalLog);
        _commands.put("find", this::find);
        _commands.put("status", this::status);
        _commands.put("checkout", this::checkout);
        _commands.put("branch", this::branch);
        _commands.put("rm-branch", this::rmBranch);
        _commands.put("reset", this::reset);
        _commands.put("merge", this::merge);
    }

    /** The path for the main gitlet directory. */
    public static final String GITLET_DIR = ".gitlet/";

}
