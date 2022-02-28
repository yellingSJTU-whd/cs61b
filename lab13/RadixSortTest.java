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

    @Test
    public void gradescope() {
        String expected = " \u0016\u001AY\u0011F\n" +
                "\u0003å\u0003³\tK|&-\u0010ïR\u0004_9\u0091C,\u0092ªe\u0006É3q?Òr44Ú©á9³\u0099³\u0011è)Ð\u0011O¿}\u0015 xI+.\u0092Á}\u0082«þ@eû&\u009DvÊø|YG8±1\u009B÷\u0094\u0006\u00ADYF}\u0010Öý-ì WàA=\u0095ÑË\u001E¥'Òw]ËÊo\u0003Ç4<\u0010\u0083I\u009D\u008D1,ÁÅ÷ë¢";
        String actual = "\u0016\u001AY\u0011F\n" +
                "\u0003å\u0003³\tK|&-\u0010ïR\u0004_9\u0091C,\u0092ªe\u0006É3q?Òr44Ú©á9³\u0099³\u0011è)Ð\u0011O¿}\u0015 xI+.\u0092Á}\u0082«þ@eû&\u009DvÊø|YG8±1\u009B÷\u0094\u0006\u00ADYF}\u0010Öý-ì WàA=\u0095ÑË\u001E¥'Òw]ËÊo\u0003Ç4<\u0010\u0083I\u009D\u008D1,ÁÅ÷ë¢ ";
    }
}
