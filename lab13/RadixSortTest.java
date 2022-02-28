import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RadixSortTest {
    @Test
    public void testLSD() {
        List<String> unsorted = List.of("set", "cat", "cad", "con",
                "bat", "can", "be", "let", "bet");
        String[] asciis = unsorted.toArray(new String[0]);
        String[] sorted = RadixSort.sort(asciis);

        String[] ref = unsorted.toArray(new String[0]);
        Arrays.sort(ref);

        assertArrayEquals(ref, sorted, Arrays.toString(sorted));
    }
}
