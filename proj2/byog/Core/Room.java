package byog.Core;

import byog.TileEngine.TETile;

import java.util.Random;

public class Room {
    private Position buttonLeft;
    private Position topRight;

    private static final int CAP = 6;

    public Room(Position buttonLeftPosition, Position topRightPosition) {
        buttonLeft = buttonLeftPosition;
        topRight = topRightPosition;
    }

    public static Room randomRoom(Random random, TETile[][] theWorld) {
        int height = theWorld.length;
        int width = theWorld[0].length;

        int x0 = RandomUtils.uniform(random, 0, width - CAP);
        int y0 = RandomUtils.uniform(random, 0, height - CAP);
        Position p0 = new Position(x0, y0);

        int x1 = RandomUtils.uniform(random, x0 + 1, x0 + CAP + 1);
        int y1 = RandomUtils.uniform(random, y0 + 1, y0 + CAP + 1);
        Position p1 = new Position(x1, y1);

        return new Room(p0, p1);
    }
}
