package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class Player {
    private Position position;

    public Player(Position place) {
        position = new Position(place.getX(), place.getY());
    }

    public boolean moveNorth(TETile[][] theWorld) {
        return move(theWorld, Direction.UP);
    }

    public boolean moveWest(TETile[][] theWorld) {
        return move(theWorld, Direction.LEFT);
    }

    public boolean moveSouth(TETile[][] theWorld) {
        return move(theWorld, Direction.DOWN);
    }

    public boolean moveEast(TETile[][] theWorld) {
        return move(theWorld, Direction.RIGHT);
    }

    private boolean move(TETile[][] theWorld, Direction direction) {
        int currentX = position.getX();
        int currentY = position.getY();
        int width = theWorld.length;
        int height = theWorld[0].length;
        int xPrim = currentX;
        int yPrim = currentY;

        switch (direction) {
            case UP -> {
                yPrim++;
            }
            case LEFT -> {
                xPrim--;
            }
            case DOWN -> {
                yPrim--;
            }
            case RIGHT -> {
                xPrim++;
            }
        }

        if (xPrim <= 0 || xPrim >= width || yPrim < 0 || yPrim >= height || theWorld[xPrim][yPrim].equals(Tileset.WALL)) {
            return false;
        }

        if (theWorld[currentX][currentY].equals(Tileset.LOCKED_DOOR)) {
            theWorld[currentX][currentY] = Tileset.UNLOCKED_DOOR;
        } else {
            theWorld[currentX][currentY] = Tileset.FLOOR;
        }
        theWorld[xPrim][yPrim] = Tileset.PLAYER;

        position = new Position(xPrim, yPrim);
        return true;
    }

    public void moveTo(Position destination) {

    }
}
