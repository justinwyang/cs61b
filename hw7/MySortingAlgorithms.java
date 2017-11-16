import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Distribution Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 1; i < k; i++) {
                for (int j = i - 1; j >= 0; j--) {
                    if (array[j + 1] < array[j]) {
                        swap(array, j + 1, j);
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 0; i < k; i++) {
                int min = array[i], idx = i;
                for (int j = i + 1; j < k; j++) {
                    if (array[j] < min) {
                        min = array[j];
                        idx = j;
                    }
                }
                swap(array, i, idx);
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            sortHelper(array, 0, k);
        }

        public void sortHelper(int[] array, int begin, int end) {
            if (end - begin < 2) {
                return;
            }
            int middle = (begin + end) / 2;
            sortHelper(array, begin, middle);
            sortHelper(array, middle, end);
            merge(array, begin, middle, end);
        }

        private void merge(int[] array, int begin, int middle, int end) {
            int[] arr1 = new int[middle - begin];
            for (int i = begin; i < middle; i++) {
                arr1[i - begin] = array[i];
            }
            int[] arr2 = new int[end - middle];
            for (int i = middle; i < end; i++) {
                arr2[i - middle] = array[i];
            }

            int i = 0, j = 0;
            while (i < middle - begin || j < end - middle) {
                int nextIdx = begin + i + j;
                if (i >= middle - begin) {
                    array[nextIdx] = arr2[j];
                    j++;
                } else if (j >= end - middle) {
                    array[nextIdx] = arr1[i];
                    i++;
                } else if (arr1[i] < arr2[j]) {
                    array[nextIdx] = arr1[i];
                    i++;
                } else {
                    array[nextIdx] = arr2[j];
                    j++;
                }
            }
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Distribution Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class DistributionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Distribution Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            PriorityQueue<Integer> queue = new PriorityQueue<>();
            for (int i = 0; i < k; i++) {
                queue.add(array[i]);
            }
            for (int i = 0; i < k; i++) {
                array[i] = queue.remove();
            }
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            sortHelper(array, 0, k);
        }

        private void sortHelper(int[] array, int begin, int end) {
            if (end - begin < 2) {
                return;
            }
            int idx = partition(array, begin, end);
            sortHelper(array, begin, idx);
            sortHelper(array, idx + 1, end);
        }

        private int partition(int[] array, int begin, int end) {
            int pivot = array[end - 1];
            int lowIdx = begin;
            for (int i = begin; i < end; i++) {
                if (array[i] <= pivot) {
                    swap(array, i, lowIdx);
                    lowIdx++;
                }
            }
            return lowIdx - 1;
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of k-bit numbers.  For
     * example, if you take k to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * k to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of k. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
