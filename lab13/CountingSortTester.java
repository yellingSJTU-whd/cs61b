import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;

public class CountingSortTester {

    /**
     * Array that will cause CountingSort.naiveCountingSort to fail, but
     * CountingSort.betterCountingSort can handle.
     **/
    private static int[] someNegative = {9, 5, -4, 2, 1, -2, 5, 3, 0, -2, 3, 1, 1};

    private static int[] allNegative = {-51, -96, -6, -38, -71, -55, -27, -68, -17, -65, -61,
        -28, -94, -34, -46, -94, -31, -47, -61, -3, -91, -69, -11, -30, -54, -66, -61, -73,
        -88, -16, -66, -43, -4, -43, -96, -63, -48, -27, -85, -9, -86, -64, -47, -25, -92, -90,
        -80, -48, -1, -5, -65, -84, -28, -38, -1, -48, -81, -92, -83, -11, -34, -92, -46, -47, -54,
        -48, -35, -34, -50, -23, -96, -29, -9, -21, -48, -9, -86, -52, -33, -62, -77, -16, -38};
    /**
     * Array that both sorts should sort successfully.
     **/
    private static int[] nonNegative = {1, 4, 0, 36, 32, 35, 9, 15, 25, 26, 33, 11,
        6, 35, 29, 20, 19, 16, 29, 9, 30, 23, 1, 20, 9, 23, 27, 16, 3, 15, 32,
        23, 25, 18, 29, 12, 4, 22, 3, 20, 13, 15, 7, 31, 9, 22, 32, 0, 28,
        35, 36, 29, 19, 30, 16, 0, 32, 19, 5, 5, 34, 25, 35, 33, 4, 35, 25, 1, 29, 23, 8, 26, 26};

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
        int[] sortedMoreNegative = CountingSort.betterCountingSort(allNegative);
        assertIsSorted(sortedMoreNegative);
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(CountingSortTester.class);
    }
}
