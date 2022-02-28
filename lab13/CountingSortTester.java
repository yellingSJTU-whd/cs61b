import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;

public class CountingSortTester {

    /**
     * Array that will cause CountingSort.naiveCountingSort to fail, but
     * CountingSort.betterCountingSort can handle.
     **/
    private static int[] someNegative = {9, 5, -4, 2, 1, -2, 5, 3, 0, -2, 3, 1, 1};

    private static int[] moreNegative = {-8, -3, -4, -6, -7, -44, -45};

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
