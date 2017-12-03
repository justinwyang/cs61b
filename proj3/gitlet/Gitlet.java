package gitlet;

import static gitlet.Utils.error;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
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
        if (command.equals("init")) {
            if (gitletDir.exists()) {
                throw error
                        ("A Gitlet version-control system already exists in the current directory.");
            }
        } else {
            if (gitletDir.exists()) {
                read();
            } else {
                throw error("Not in an initialized Gitlet directory.");
            }
        }
        _commands.get(command).accept(operands);
        write();
    }

    /** Reads in the necessary metadata to run Gitlet. */
    public void read() {
        _branch = Branch.readBranch(Utils.readObject(new File(GITLET_DIR + ".branch"), String.class));
    }

    /** Saves the metadata for the next run of Gitlet. */
    public void write() {
        _branch.writeBranch();
        Utils.writeObject(new File(GITLET_DIR + ".branch"), _branch.name());
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
        checkOperands(operands, 0);
        File gitletDir = new File(GITLET_DIR);
        gitletDir.mkdir();
        File branchDir = new File(Branch.BRANCH_DIR);
        branchDir.mkdir();
        File commitDir = new File(Commit.COMMIT_DIR);
        commitDir.mkdir();
        File blobDir = new File(Blob.BLOB_DIR);
        blobDir.mkdir();
        Commit head = new Commit();
        _branch = new Branch(head, "master");
    }

    public void add(String[] operands) {
        checkOperands(operands, 1);
        Branch.readBranch(operands[0]).add(operands[0]);
    }

    public void commit(String[] operands) {
        checkOperands(operands, 1);
        Branch.readBranch(operands[0]).commit(operands[0]);
    }

    public void rm(String[] operands) {
        checkOperands(operands, 1);
        Branch.readBranch(operands[0]).remove(operands[0]);
    }

    public void log(String[] operands) {
        checkOperands(operands, 0);
        for (Commit commit = Branch.readBranch(operands[0]).head(); commit != null; commit = commit.parent()) {
            System.out.print(commit);
        }
    }

    public void globalLog(String[] operands) {
        checkOperands(operands, 0);
        for (String commitID: Utils.plainFilenamesIn(Commit.COMMIT_DIR)) {
            System.out.print(Commit.readCommit(commitID));
        }
    }

    public void find(String[] operands) {
        checkOperands(operands, 1);
        for (String commitID: Utils.plainFilenamesIn(Commit.COMMIT_DIR)) {
            Commit commit = Commit.readCommit(commitID);
            if (commit.message().equals(operands[0])) {
                System.out.print(commit);
            }
        }
    }

    public void status(String[] operands) {
        checkOperands(operands, 0);

        System.out.println("=== Branches ===");
        System.out.println("*" + _branch);
        for (String filename: Utils.plainFilenamesIn(Branch.BRANCH_DIR)) {
            if (!filename.equals(_branch)) {
                System.out.println(filename);
            }
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        for(Map.Entry<String, Blob> entry: _branch.staged().entrySet()) {
            System.out.println(entry.getKey());
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        for(String entry: _branch.removed()) {
            System.out.println(entry);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");





    }

    public void checkout(String[] operands) {

    }

    public void branch(String[] operands) {
        checkOperands(operands, 1);
        if(Utils.plainFilenamesIn(Branch.BRANCH_DIR).contains(operands[0])) {
            throw error("A branch with that name already exists.");
        }
        Branch other = new Branch(Branch.readBranch(operands[0]).head(), operands[0]);
        other.writeBranch();
    }

    public void rmBranch(String[] operands) {
        checkOperands(operands, 1);
        if (operands[0].equals(_branch)) {
            throw error("Cannot remove the current branch.");
        }
        File file = new File(Branch.BRANCH_DIR + operands[0]);
        if (!file.exists()) {
            throw error("A branch with that name does not exist.");
        }
        file.delete();
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

    /** The current Branch. */
    private Branch _branch;

    /** The path for the main Gitlet directory. */
    static final String GITLET_DIR = ".gitlet/";
}
