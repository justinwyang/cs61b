package db61b;

import org.junit.Test;
import ucb.junit.textui;
import java.util.ArrayList;
import java.util.Arrays;

/** The suite of all JUnit tests for the qirkat package.
 *  @author P. N. Hilfinger
 */
public class UnitTest {

    @Test
    public void testReadWrite() {
        System.out.println("testReadWrite\n");
        Table table = Table.readTable("testing/enrolled");
        table.print();
        table.writeTable("enrolledOutput");
        System.out.println();
    }

    @Test
    public void testSelectSingle() {
        System.out.println("testSelectSingle\n");
        Table table = Table.readTable("testing/students");
        ArrayList<String> columnNames = new ArrayList<>(Arrays.asList(
                "SID", "Lastname", "Firstname", "Major"));

        ArrayList<Condition> conditions1 = new ArrayList<>(Arrays.asList(
                new Condition(new Column("Lastname", table),
                        "<", new Column("Firstname", table))));
        Table selected1 = table.select(columnNames, conditions1);
        selected1.print();
        selected1.writeTable("selectSingle1");
        System.out.println();

        ArrayList<Condition> conditions2 = new ArrayList<>(
                Arrays.asList(new Condition(new Column("Lastname", table),
                        "<", "L")));
        Table selected2 = table.select(columnNames, conditions2);
        selected2.print();
        selected2.writeTable("selectSingle2");
        System.out.println();
    }

    @Test
    public void testSelectDouble() {
        System.out.println("testSelectDouble\n");
        Table table1 = Table.readTable("testing/students");
        Table table2 = Table.readTable("testing/enrolled");
        ArrayList<String> columnNames = new ArrayList<>(
                Arrays.asList("SID", "Firstname", "Grade"));

        ArrayList<Condition> conditions1 = new ArrayList<>(Arrays.asList(
                new Condition(new Column("Lastname", table1, table2),
                        "<", new Column("Firstname", table1, table2))));
        Table selected1 = table1.select(table2, columnNames, conditions1);
        selected1.print();
        selected1.writeTable("selectDouble1");
        System.out.println();

        ArrayList<Condition> conditions2 = new ArrayList<>(
                Arrays.asList(new Condition(new Column(
                        "Lastname", table1, table2), "=", "Chan")));
        Table selected2 = table1.select(table2, columnNames, conditions2);
        selected2.print();
        selected2.writeTable("selectDouble2");
        System.out.println();
    }

    @Test
    public void testException() {
        System.out.println("testException\n");
        Table table = Table.readTable("testing/students");
        ArrayList<String> columnNames = new ArrayList<>(
                Arrays.asList("Lastname", "Major"));
        ArrayList<Condition> conditions = new ArrayList<>(
                Arrays.asList(new Condition(new Column(
                        "Minor", table), "=", "EECS")));
        Table selected = table.select(table, columnNames, conditions);
        System.out.println();
    }


    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses();
    }

}
