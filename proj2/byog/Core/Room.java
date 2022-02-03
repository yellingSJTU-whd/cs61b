package byog.Core;

import byog.TileEngine.TETile;

import java.util.List;
import java.util.Random;

public class Room {
    private Position buttonLeft;
    private Position topRight;

    private static final int CAP = 6;

    public Room(Position buttonLeftPosition, Position topRightPosition) {
        buttonLeft = buttonLeftPosition;
        topRight = topRightPosition;
    }

    public Position getButtonLeft(){
        return buttonLeft;
    }

    public Position getTopRight(){
        return topRight;
    }

    public static Room randomRoom(Random random, TETile[][] theWorld) {
        int height = theWorld[0].length;
        int width = theWorld.length;

        int x0 = RandomUtils.uniform(random, 3, width - CAP);
        int y0 = RandomUtils.uniform(random, 3, height - CAP);
        Position p0 = new Position(x0, y0);

        int x1 = RandomUtils.uniform(random, x0 + 1, x0 + CAP + 1);
        int y1 = RandomUtils.uniform(random, y0 + 1, y0 + CAP + 1);
        Position p1 = new Position(x1, y1);

        return new Room(p0, p1);
    }

    public boolean overLap(Room another) {
        if (another == null) {
            return false;
        }
        if (this.equals(another)) {
            return true;
        }
        return buttonLeft.getY() <= another.topRight.getY() &&
                topRight.getY() >= another.buttonLeft.getY() &&
                buttonLeft.getX() <= another.topRight.getX() &&
                topRight.getX() >= another.buttonLeft.getX();
    }

    public boolean overLap(List<Room> rooms) {
        if (rooms == null || rooms.size() == 0) {
            return false;
        }
        for (Room room : rooms) {
            if (overLap(room)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if ((!(o instanceof Room))) {
            return false;
        }
        return buttonLeft.equals(((Room) o).buttonLeft) && topRight.equals(((Room) o).topRight);
    }

    public boolean contains(Position p) {
        return buttonLeft.getX() <= p.getX() && p.getX() <= topRight.getX()
                && buttonLeft.getY() <= p.getY() && p.getY() <= topRight.getY();
    }
}
