import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.LinkedList;

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
 *
 * @author Justin Yang
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

        /** Sort helper to be used for MergeSort.
         *
         * @param array the array to sort from
         * @param begin the begin index
         * @param end the ending index
         */
        public void sortHelper(int[] array, int begin, int end) {
            if (end - begin < 2) {
                return;
            }
            int middle = (begin + end) / 2;
            sortHelper(array, begin, middle);
            sortHelper(array, middle, end);
            merge(array, begin, middle, end);
        }

        /** A merge method for merge sort.
         *
         * @param array the array to sort from
         * @param begin the begin index
         * @param middle the dividing index
         * @param end the end index
         */
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
            int maxNum = -1;
            int[] copy = new int[k];
            for (int i = 0; i < k; i++) {
                maxNum = Math.max(array[i], maxNum);
                copy[i] = array[i];
            }

            int[] count = new int[maxNum + 1];
            for (int i = 0; i < k; i++) {
                count[array[i]]++;
            }

            int[] runningSum = new int[maxNum + 1];
            runningSum[0] = 0;
            for (int i = 1; i < runningSum.length; i++) {
                runningSum[i] = runningSum[i - 1] + count[i - 1];
            }

            for (int i: copy) {
                array[runningSum[i]] = i;
                runningSum[i]++;
            }
        }

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

        /** A helper method to be used for quicksort.
         *
         * @param array the array to sort from
         * @param begin the begin index
         * @param end the end index
         */
        private void sortHelper(int[] array, int begin, int end) {
            if (end - begin < 2) {
                return;
            }
            int idx = partition(array, begin, end);
            sortHelper(array, begin, idx);
            sortHelper(array, idx + 1, end);
        }

        /** A partition method for quicksort.
         *
         * @param array the array to sort form
         * @param begin the begin index
         * @param end the end index
         * @return the index of the pivot
         */
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
            for (int place = 0; place < k; place++) {
                sortHelper(a, place, k);
            }
        }

        /** A sort helper method for LSDSort.
         *
         * @param a the array to sort from
         * @param place the binary place to sort by
         * @param k the total number of bits
         */
        private void sortHelper(int[] a, int place, int k) {
            LinkedList<Integer>[] buckets = new LinkedList[2];
            for (int i = 0; i < buckets.length; i++) {
                buckets[i] = new LinkedList<Integer>();
            }

            for (int i = 0; i < k; i++) {
                buckets[getDigit(a[i], place)].add(a[i]);
            }

            int idx = 0;
            for (LinkedList l: buckets) {
                for (Object num: l) {
                    a[idx] = (Integer) num;
                    idx++;
                }
            }
        }

        /** A helper method to obtain the digit at a certain place.
         *
         * @param num the number to analyze.
         * @param place the place of the bit needed
         * @return the obtained bit
         */
        private int getDigit(int num, int place) {
            return (num >>> place) & 1;
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
            sortHelper(a, 0, k, k - 1);
        }

        /** A sort helped method for MSDSort.
         *
         * @param a the array to sort from
         * @param begin the begin index to sort
         * @param end the end index to sort
         * @param place the bit place
         */
        private void sortHelper(int[] a, int begin, int end, int place) {
            if (place < 0) {
                return;
            }
            if (end - begin < 2) {
                return;
            }

            LinkedList<Integer>[] buckets = new LinkedList[2];
            for (int i = 0; i < buckets.length; i++) {
                buckets[i] = new LinkedList<Integer>();
            }

            for (int i = begin; i < end; i++) {
                buckets[getDigit(a[i], place)].add(a[i]);
            }

            int idx = begin;
            for (LinkedList l: buckets) {
                int tempStart = idx;
                for (Object num: l) {
                    a[idx] = (Integer) num;
                    idx++;
                }
                sortHelper(a, tempStart, idx, place - 1);
            }
        }

        /** A helper method to obtain the digit at a certain place.
         *
         * @param num the number to analyze.
         * @param place the place of the bit needed
         * @return the obtained bit
         */
        private int getDigit(int num, int place) {
            return (num >>> place) & 1;
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
