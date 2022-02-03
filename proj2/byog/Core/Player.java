package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Player {
    private Position position;

    public Player(Position place) {
        position = new Position(place.getX(), place.getY());
    }

    public boolean moveNorth(TETile[][] theWorld, String gameInfo) {
        return move(theWorld, Direction.UP, gameInfo);
    }

    public boolean moveWest(TETile[][] theWorld, String gameInfo) {
        return move(theWorld, Direction.LEFT, gameInfo);
    }

    public boolean moveSouth(TETile[][] theWorld, String gameInfo) {
        return move(theWorld, Direction.DOWN, gameInfo);
    }

    public boolean moveEast(TETile[][] theWorld, String gameInfo) {
        return move(theWorld, Direction.RIGHT, gameInfo);
    }

    private boolean move(TETile[][] theWorld, Direction direction, String gameInfo) {
        int currentX = position.getX();
        int currentY = position.getY();
        int width = theWorld.length;
        int height = theWorld[0].length;
        int xPrim = currentX;
        int yPrim = currentY;
        String suffix = null;

        switch (direction) {
            case UP -> {
                yPrim++;
                suffix = "north";
            }
            case LEFT -> {
                xPrim--;
                suffix = "west";
            }
            case DOWN -> {
                yPrim--;
                suffix = "south";
            }
            case RIGHT -> {
                xPrim++;
                suffix = "east";
            }
        }

        if (!(0 < xPrim && xPrim < width && 0 < yPrim && yPrim < height)) {
            gameInfo = "can't go" + suffix;
            return false;
        }

        if (theWorld[currentX][currentY].equals(Tileset.LOCKED_DOOR)) {
            theWorld[currentX][currentY] = Tileset.UNLOCKED_DOOR;
        } else {
            theWorld[currentX][currentY] = Tileset.FLOOR;
        }
        theWorld[xPrim][yPrim] = Tileset.PLAYER;

        position = new Position(xPrim, yPrim);
        gameInfo = "went" + suffix;
        return true;
    }

    public void moveTo(Position destination) {

    }
}
