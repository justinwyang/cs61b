package gitlet;

import ucb.junit.textui;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

/** The suite of all JUnit tests for the gitlet package.
 *
 *  Credits: the delete method is taken from http://roufid.com/
 *
 *  @author Justin Yang
 */
public class UnitTest {

    /** Run the JUnit tests in the loa package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /** Tests the Commit class. */
    @Test
    public void commitTest() {
        Main.main("init");
        Main.main("add", "Makefile");
        Main.main("commit", "committed");
        assertEquals(2,
                Utils.plainFilenamesIn(".gitlet/commits").size());

        try {
            delete(new File(".gitlet"));
        } catch (IOException e) {
            System.out.println("Cannot delete");
        }
    }

    /** Tests the Branch class. */
    @Test
    public void branchTest() {
        Main.main("init");
        Main.main("branch", "next");
        assertEquals(2,
                Utils.plainFilenamesIn(".gitlet/branches").size());

        try {
            delete(new File(".gitlet"));
        } catch (IOException e) {
            System.out.println("Cannot delete");
        }
    }

    /** Tests the Blob class. */
    @Test
    public void blobTest() {
        Main.main("init");
        Main.main("add", "Makefile");
        assertEquals(1,
                Utils.plainFilenamesIn(".gitlet/blobs").size());

        try {
            delete(new File(".gitlet"));
        } catch (IOException e) {
            System.out.println("Cannot delete");
        }
    }

    /**
     * Delete a file or a directory and its children.
     * @param file The directory to delete.
     * @throws IOException Exception when problem
     * occurs during deleting the directory.
     */
    private static void delete(File file) throws IOException {
        for (File childFile : file.listFiles()) {
            if (childFile.isDirectory()) {
                delete(childFile);
            } else {
                if (!childFile.delete()) {
                    throw new IOException();
                }
            }
        }
        if (!file.delete()) {
            throw new IOException();
        }
    }

}


