package gitlet;

import static gitlet.Utils.error;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
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
                throw error("A Gitlet version-control system "
                        + "already exists in the current directory.");
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
        _branch = Branch.readBranch(Utils.readObject
                (new File(GITLET_DIR + ".branch"), String.class));
    }

    /** Saves the metadata for the next run of Gitlet. */
    public void write() {
        _branch.writeBranch();
        Utils.writeObject(new File(GITLET_DIR + ".branch"), _branch.name());
    }

    /** Checks for the correct number of operands.
     *
     * @param operands the operands to check
     * @param num the required number of operands
     */
    public void checkOperands(String[] operands, int num) {
        if (num != operands.length) {
            throw error(INCORRECT_OPERANDS);
        }
    }

    /** Performs an init command.
     *
     * @param operands operands for init
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

    /** Performs an add command.
     *
     * @param operands operands for add
     */
    public void add(String[] operands) {
        checkOperands(operands, 1);
        _branch.add(operands[0]);
    }

    /** Performs a commit command.
     *
     * @param operands operands for commit
     */
    public void commit(String[] operands) {
        checkOperands(operands, 1);
        _branch.commit(operands[0]);
    }

    /** Performs a rm command.
     *
     * @param operands operands for rm
     */
    public void rm(String[] operands) {
        checkOperands(operands, 1);
        _branch.remove(operands[0]);
    }

    /** Performs a log command.
     *
     * @param operands operands for log
     */
    public void log(String[] operands) {
        checkOperands(operands, 0);
        for (Commit commit = _branch.head();
             commit != null; commit = commit.parent()) {
            System.out.print(commit);
        }
    }

    /** Performs a globalLog command.
     *
     * @param operands operands for globalLog
     */
    public void globalLog(String[] operands) {
        checkOperands(operands, 0);
        for (String commitID: Utils.plainFilenamesIn(Commit.COMMIT_DIR)) {
            System.out.print(Commit.readCommit(commitID));
        }
    }

    /** Performs a find command.
     *
     * @param operands operands for find
     */
    public void find(String[] operands) {
        checkOperands(operands, 1);
        boolean found = false;
        for (String commitID: Utils.plainFilenamesIn(Commit.COMMIT_DIR)) {
            Commit commit = Commit.readCommit(commitID);
            if (commit.message().equals(operands[0])) {
                System.out.print(commit);
                found = true;
            }
        }
        if (!found) {
            throw error("Found no commit with that message.");
        }
    }

    /** Performs a status command.
     *
     * @param operands operands for status
     */
    public void status(String[] operands) {
        checkOperands(operands, 0);

        sortAndPrint(Utils.plainFilenamesIn(Branch.BRANCH_DIR),
                "=== Branches ===", true);

        sortAndPrint(new ArrayList<>(_branch.staged().keySet()),
                "=== Staged Files ===", false);

        sortAndPrint(new ArrayList<>(_branch.removed()),
                "=== Removed Files ===", false);

        sortAndPrint(_branch.unstaged(),
                "=== Modifications Not Staged For Commit ===", false);

        sortAndPrint(_branch.untracked(),
                "=== Untracked Files ===", false);
    }

    /** Sorts the entries of list and prints them for status.
     *
     * @param list the list to sort and print
     * @param header the header label
     * @param asterisk whether to add asterisk before the current branch
     */
    private void sortAndPrint(List<String> list,
                              String header, boolean asterisk) {
        System.out.println(header);
        Collections.sort(list);
        for (String entry: list) {
            if (asterisk && _branch.name().equals(entry)) {
                System.out.print("*");
            }
            System.out.println(entry);
        }
        System.out.println();
    }

    /** Performs a checkout command.
     *
     * @param operands operands for checkout
     */
    public void checkout(String[] operands) {
        if (operands.length == 2 && operands[0].equals("--")) {
            checkoutCommitFile(_branch.head().commitID(), operands[1]);
        } else if (operands.length == 3 && operands[1].equals("--")) {
            checkoutCommitFile(operands[0], operands[2]);
        } else if (operands.length == 1) {
            if (!Branch.exists(operands[0])) {
                throw error("No such branch exists.");
            }
            if (operands[0].equals(_branch.name())) {
                throw error("No need to checkout the current branch.");
            }
            checkoutBranch(operands[0],
                    Branch.readBranch(operands[0]).head().commitID());
        } else {
            throw error(INCORRECT_OPERANDS);
        }
    }

    /** Checks out a file from a specific Commit.
     *
     * @param prefix The CommitID prefix to checkout from
     * @param filename The filename to checkout
     */
    public void checkoutCommitFile(String prefix, String filename) {
        String found = Commit.findCommit(prefix);
        HashMap<String, Blob> blobs = Commit.readCommit(found).blobs();
        if (!blobs.containsKey(filename)) {
            throw error("File does not exist in that commit.");
        }
        blobs.get(filename).restore();
    }

    /** Checks out a Branch.
     *
     * @param branchName the Branch name to checkout
     * @param commitID the commitID to checkout from
     */
    public void checkoutBranch(String branchName, String commitID) {
        Commit commit = Commit.readCommit(commitID);
        for (Map.Entry<String, Blob> entry: commit.blobs().entrySet()) {
            String filename = entry.getKey();
            Blob blob = entry.getValue();
            boolean delete = !new File(filename).exists();
            if (!_branch.tracked().containsKey(filename)) {
                if (delete || !Blob.sha1(filename).equals(blob.hash())) {
                    throw error("There is an untracked file in the way;"
                            + " delete it or add it first.");
                }
            }
            if (delete) {
                Utils.restrictedDelete(filename);
            }
        }
        commit.restore();
        _branch.writeBranch();
        _branch = Branch.readBranch(branchName);
        _branch.clearStage();
    }

    /** Performs a branch command.
     *
     * @param operands operands for branch
     */
    public void branch(String[] operands) {
        checkOperands(operands, 1);
        if (Branch.exists(operands[0])) {
            throw error("A branch with that name already exists.");
        }
        Branch other =
                new Branch(Branch.readBranch(operands[0]).head(), operands[0]);
        other.writeBranch();
    }

    /** Performs a rmBranch command.
     *
     * @param operands operands for rmBranch
     */
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

    /** Performs a reset command.
     *
     * @param operands operands for reset
     */
    public void reset(String[] operands) {
        checkOperands(operands, 1);
        String commitID = Commit.findCommit(operands[0]);
        checkoutBranch(_branch.name(), commitID);
        _branch.setHead(commitID);
    }

    /** Performs a merge command.
     *
     * @param operands operands for merge
     */
    public void merge(String[] operands) {
        checkOperands(operands, 1);
        if (!_branch.staged().isEmpty() || !_branch.removed().isEmpty()) {
            throw error("You have uncommitted changes.");
        }
        if (!Branch.exists(operands[0])) {
            throw error("A branch with that name does not exist.");
        }
        if (_branch.name().equals(operands[0])) {
            throw error("Cannot merge a branch with itself.");
        }

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

    /** Error message for incorrect operands. */
    private static final String INCORRECT_OPERANDS = "Incorrect operands.";
}
