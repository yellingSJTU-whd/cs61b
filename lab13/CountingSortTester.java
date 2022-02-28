import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;

public class CountingSortTester {

    /**
     * Array that will cause CountingSort.naiveCountingSort to fail, but
     * CountingSort.betterCountingSort can handle.
     **/
    private static int[] someNegative = {9, 5, -4, 2, 1, -2, 5, 3, 0, -2, 3, 1, 1};

    private static int[] moreNegative = {-95, -94, -94, -93, -93, -92, -91, -89, -88, -85, -84, -83, -81, -81, -79, -79,
            -25, -24, -22, -20, -19, -17, -14, -13, -13, -12, -10, -10, -10, -9, -5, -4, -3, -2, -2, -2, 0, 0, -1,
            -77, -76, -76, -76, -74, -71, -69, -68, -67, -65, -64, -64, -63, -62, -61, -61, -60, -59, -59, -55, -55, -54,
            -52, -51, -50, -50, -47, -45, -43, -42, -42, -41, -40, -40, -39, -34, -33, -32, -31, -31, -28, -28, -27, -26};

    /**
     * Array that both sorts should sort successfully.
     **/
    private static int[] nonNegative = {9, 5, 2, 1, 5, 3, 0, 3, 1, 1};

    public static void assertIsSorted(int[] a) {
        int previous = Integer.MIN_VALUE;
        for (int x : a) {
            assertTrue("After sorting:\n" + Arrays.toString(a), x >= previous);
            previous = x;
        }
    }

    @Test
    public void testNaiveWithNonNegative() {
        int[] sortedNonNegative = CountingSort.naiveCountingSort(nonNegative);
        assertIsSorted(sortedNonNegative);
    }

    // This test should PASS to indicate that the naive solution is in fact WRONG
    @Test
    public void testNaiveWithSomeNegative() {
        try {
            int[] sortedSomeNegative = CountingSort.naiveCountingSort(someNegative);
            fail("Naive counting sort should not sort arrays with negative numbers.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Great! Exception happened as we expected,"
                    + "since this sort is broken for negative numbers.");
        }
    }

    @Test
    public void testBetterWithNonNegative() {
        int[] sortedNonNegative = CountingSort.betterCountingSort(nonNegative);
        assertIsSorted(sortedNonNegative);
    }

    @Test
    public void testBetterWithSomeNegative() {
        int[] sortedSomeNegative = CountingSort.betterCountingSort(someNegative);
        assertIsSorted(sortedSomeNegative);
    }

    @Test
    public void testBetterWithMoreNegative() {
        int[] sortedMoreNegative = CountingSort.betterCountingSort(moreNegative);
        assertIsSorted(sortedMoreNegative);
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(CountingSortTester.class);
    }
}
