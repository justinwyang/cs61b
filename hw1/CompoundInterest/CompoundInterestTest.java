import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers. */
        assertEquals(30, CompoundInterest.numYears(2045));
        assertEquals(15, CompoundInterest.numYears(2030));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(12.544, CompoundInterest.futureValue(10, 12, 2017), tolerance);
        assertEquals(18.522, CompoundInterest.futureValue(16, 5, 2018), tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(11.881, CompoundInterest.futureValueReal(10, 12, 2017, 3), tolerance);
        assertEquals(16.000, CompoundInterest.futureValueReal(16, 5, 2018, 5), tolerance);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(16550, CompoundInterest.totalSavings(5000, 2017, 10), tolerance);
        assertEquals(43.1013, CompoundInterest.totalSavings(10, 2018, 5), tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;
        assertEquals(15762.5, CompoundInterest.totalSavingsReal(5000, 2017, 10,5), tolerance);
        assertEquals(41.2161, CompoundInterest.totalSavingsReal(10, 2018, 5,3), tolerance);
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
