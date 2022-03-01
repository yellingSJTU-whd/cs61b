import java.util.Arrays;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        System.out.println("before radix sort: " + Arrays.toString(asciis));
        if (asciis == null || asciis.length < 2) {
            return asciis;
        }
        //LSD
        int digits = Integer.MIN_VALUE;
        for (String str : asciis) {
            digits = Math.max(digits, str.length());
        }
        System.out.println("digits= " + digits);
        for (int i = digits - 1; i >= 0; i--) {
            sortHelperLSD(asciis, i);
        }

        //MSD
//        int digits = Integer.MIN_VALUE;
//        for (String str : asciis) {
//            digits = Math.max(digits, str.length());
//        }
        return asciis;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     *
     * @param asciis Input array of Strings
     * @param index  The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        int[] count = new int[65536];
        for (String ascii : asciis) {
            int transfer;
            if (index >= ascii.length()) {
                transfer = ' ';
            } else {
                transfer = ascii.charAt(index);
            }
            count[transfer]++;
        }

        int[] start = new int[count.length];
        int pos = 0;
        for (int i = 0; i < count.length; i++) {
            start[i] = pos;
            pos += count[i];
        }

        String[] ref = Arrays.copyOf(asciis, asciis.length);
        for (String ascii : ref) {
            int transfer;
            if (index >= ascii.length()) {
                transfer = ' ';
            } else {
                transfer = ascii.charAt(index);
            }
            int place = start[transfer];
            asciis[place] = ascii;
            start[transfer]++;
        }
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start  int for where to start sorting in this method (includes String at start)
     * @param end    int for where to end sorting in this method (does not include String at end)
     * @param index  the index of the character the method is currently sorting on
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
