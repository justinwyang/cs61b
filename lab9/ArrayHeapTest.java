import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArrayHeapTest {

    /** Basic test of adding, checking, and removing two elements from a heap */
    @Test
    public void simpleTest() {
        ArrayHeap<String> pq = new ArrayHeap<>();
        pq.insert("Qir", 2);
        pq.insert("Kat", 1);
        assertEquals(2, pq.size());

        String first = pq.removeMin();
        assertEquals("Kat", first);
        assertEquals(1, pq.size());

        String second = pq.removeMin();
        assertEquals("Qir", second);
        assertEquals(0, pq.size());
    }

    @Test
    public void complexTest() {
        ArrayHeap<Integer> pq = new ArrayHeap<>();
        int[] values = new int[]{20, 17, 9, 5, 4, 0, -1, 1};
        for (int i : values) {
            pq.insert(i, -i);
        }
        assertEquals(8, pq.size());
        assertEquals(20, (int)pq.peek().item());
        pq.insert(8, -8);
        pq.insert(18, -18);
        assertEquals(10, pq.size());
        assertEquals(20, (int)pq.removeMin());
        assertEquals(18, (int)pq.peek().item());
        pq.changePriority(5, -40);
        assertEquals(5, (int)pq.peek().item());
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArrayHeapTest.class));
    }
}
