import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestFlik {
    @Test
    void testIsSameNumber() {
        Integer a = 128;
        Integer b = 128;
        assertFalse(Flik.isSameNumber(a, b));
    }
}
