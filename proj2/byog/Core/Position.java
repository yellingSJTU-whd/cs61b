package byog.Core;

import byog.TileEngine.TETile;

import java.util.ArrayList;
import java.util.List;

public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position another)) {
            return false;
        }
        return x == another.getX() && y == another.getY();
    }

    public boolean belongsTo(TETile[][] theWorld, TETile type) {
        return theWorld[x][y].equals(type);
    }

    public boolean outOfTheWorld(TETile[][] theWorld) {
        int width = theWorld.length;
        int height = theWorld[0].length;
        return (x <= 0 || x >= width || y <= 0 || y >= height);
    }

    public List<Position> oddNeighbours(TETile[][] theWorld, TETile neighbourType) {
        return neighbours(theWorld, neighbourType, false, false);
    }


    public List<Position> evenNeighbours(TETile[][] theWorld, TETile neighbourType) {
        return neighbours(theWorld, neighbourType, true, false);
    }

    private List<Position> neighbours(TETile[][] theWorld, TETile neighbourType, boolean isEven, boolean diagonal) {
        if (outOfTheWorld(theWorld)) {
            throw new IllegalArgumentException("invalid position (" + x + "," + y + ")");
        }

        List<Position> neighbours = new ArrayList<>(4);
        int width = theWorld.length;
        int height = theWorld[0].length;
        int delta = isEven ? 2 : 1;

        Position left = new Position(x - delta, y);
        Position right = new Position(x + delta, y);
        Position top = new Position(x, y + delta);
        Position button = new Position(x, y - delta);

        Position topLeft = new Position(x - delta, y + delta);
        Position topRight = new Position(x + delta, y + delta);
        Position buttonLeft = new Position(x - delta, y - delta);
        Position buttonRight = new Position(x + delta, y - delta);

        if (x > 1 && left.belongsTo(theWorld, neighbourType)) {
            neighbours.add(left);
        }
        if (x < width - 1 && right.belongsTo(theWorld, neighbourType)) {
            neighbours.add(right);
        }
        if (y > 1 && button.belongsTo(theWorld, neighbourType)) {
            neighbours.add(button);
        }
        if (y < height - 1 && top.belongsTo(theWorld, neighbourType)) {
            neighbours.add(top);
        }

        if (diagonal) {
            if (x > 1 && y > 1 && buttonLeft.belongsTo(theWorld, neighbourType)) {
                neighbours.add(buttonLeft);
            }
            if (x < width - 1 && y > 1 && buttonRight.belongsTo(theWorld, neighbourType)) {
                neighbours.add(buttonRight);
            }
            if (x > 1 && y < height - 1 && topLeft.belongsTo(theWorld, neighbourType)) {
                neighbours.add(topLeft);
            }
            if (x < width - 1 && y < height - 1 && topRight.belongsTo(theWorld, neighbourType)) {
                neighbours.add(topRight);
            }
        }

        return neighbours;
    }

    public List<Position> diagonalNeighbours(TETile[][] theWorld, TETile neighbourType) {
        return neighbours(theWorld, neighbourType, false, true);
    }
}
