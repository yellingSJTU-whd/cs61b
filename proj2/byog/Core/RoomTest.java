package byog.Core;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoomTest {

    @org.junit.jupiter.api.Test
    void overLap() {
        Room r1 = new Room(new Position(0, 0), new Position(10, 10));
        Room r2 = new Room(new Position(9, 9), new Position(15, 15));

        assertTrue(r1.overLap(r2));
    }
}
