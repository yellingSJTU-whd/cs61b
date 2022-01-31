package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 81;
    public static final int HEIGHT = 31;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        TETile[][] finalWorldFrame = null;
        return finalWorldFrame;
    }

    /**
     * Intern routine to generate the 2D tile game world, pseudo-randomly.
     *
     * @param seed pseudo-random seed
     * @return the world generated
     * @source https://zhuanlan.zhihu.com/p/27381213
     */
    private TETile[][] generateWorld(long seed) {
        //1. init
        TETile[][] theWorld = new TETile[WIDTH][HEIGHT];
        for (TETile[] column : theWorld) {
            Arrays.fill(column, Tileset.NOTHING);
        }

        //2. generate rooms pseudo-randomly
        Random random = new Random(seed);
        List<Room> rooms = generateRooms(theWorld, random);

        //3. generate halls from button left of the map
        Position start = new Position(1, 1);
        theWorld[1][1] = Tileset.FLOOR;
        List<Position> deadEnds = generateHalls(start, theWorld, new ArrayList<>());

        //4. connect rooms and halls
        connectToHalls(rooms, theWorld, random);

        //5. subtract deadEnds pseudo-randomly
        List<Position> newEnds = removeDeadEnds(deadEnds, theWorld, random, new ArrayList<>(deadEnds.size()));

        //6. generate walls
        for (int x = 0; x < theWorld[0].length; x++) {
            for (int y = 0; y < theWorld.length; y++) {
                if (isWall(theWorld, x, y)) {
                    theWorld[x][y] = Tileset.WALL;
                }
            }
        }

        //7. set beginning position
        int luckyNum = RandomUtils.uniform(random, newEnds.size());
        Position beginning = newEnds.get(luckyNum);
        theWorld[beginning.getX()][beginning.getY()] = Tileset.LOCKED_DOOR;


        return theWorld;
    }

    private boolean isWall(TETile[][] theWorld, int x, int y) {
        if (!theWorld[x][y].equals(Tileset.NOTHING)) {
            return false;
        }
        Position current = new Position(x, y);
        List<Position> neighbourFloorTiles = current.diagonalNeighbours(theWorld, Tileset.FLOOR);
        List<Position> neighbourRoomTiles = current.diagonalNeighbours(theWorld, Tileset.ROOM);
        return neighbourFloorTiles.size() + neighbourRoomTiles.size() > 0;
    }

    private List<Position> removeDeadEnds(List<Position> deadEnds, TETile[][] theWorld, Random random, List<Position> newEnds) {

        if (deadEnds == null || deadEnds.size() == 0) {
            return newEnds;
        }
        for (Position deadEnd : deadEnds) {
            if (RandomUtils.bernoulli(random, 0.75)) {
                theWorld[deadEnd.getX()][deadEnd.getY()] = Tileset.NOTHING;
                List<Position> neighbours = deadEnd.oddNeighbours(theWorld, Tileset.FLOOR);
                removeDeadEnds(neighbours, theWorld, random, newEnds);
            } else {
                newEnds.add(deadEnd);
            }
        }
        return newEnds;
    }


    private void connectToHalls(List<Room> rooms, TETile[][] theWorld, Random random) {
        for (Room room : rooms) {
            connectToHalls(room, theWorld, random);
        }
    }

    private void connectToHalls(Room room, TETile[][] theWorld, Random random) {
        Position buttonRight = new Position(room.getTopRight().getX(), room.getButtonLeft().getY());
        Position topLeft = new Position(room.getButtonLeft().getX(), room.getTopRight().getY());

        connectToHalls(room.getButtonLeft(), buttonRight, Direction.DOWN, theWorld, random);
        connectToHalls(room.getButtonLeft(), topLeft, Direction.LEFT, theWorld, random);
        connectToHalls(topLeft, room.getTopRight(), Direction.UP, theWorld, random);
        connectToHalls(buttonRight, room.getTopRight(), Direction.RIGHT, theWorld, random);
    }

    private void connectToHalls(Position start, Position end, Direction direction, TETile[][] theWorld, Random random) {
        List<Position> candidates = new ArrayList<>();

        if (direction.equals(Direction.DOWN) || direction.equals(Direction.UP)) {
            int y = direction.equals(Direction.DOWN) ? start.getY() - 2 : start.getY() + 2;
            for (int x = start.getX(); x <= end.getX(); x++) {
                if (theWorld[x][y].equals(Tileset.FLOOR)) {
                    candidates.add(new Position(x, y));
                }
            }
            if (connector(candidates, random) != null) {
                int yPrim = y < start.getY() ? y + 1 : y - 1;
                theWorld[start.getX()][yPrim] = Tileset.FLOOR;
            }
        } else {
            int x = direction.equals(Direction.LEFT) ? start.getX() - 2 : start.getX() + 2;
            for (int y = start.getY(); y <= end.getY(); y++) {
                if (theWorld[x][y].equals(Tileset.FLOOR)) {
                    candidates.add(new Position(x, y));
                }
            }
            if (connector(candidates, random) != null) {
                int xPrim = x < start.getX() ? x + 1 : x - 1;
                theWorld[xPrim][start.getY()] = Tileset.FLOOR;
            }
        }
    }

    private Position connector(List<Position> candidates, Random random) {
        Position connector = null;
        if (candidates.size() != 0 && RandomUtils.bernoulli(random)) {
            int luckyNum = RandomUtils.uniform(random, candidates.size());
            connector = candidates.get(luckyNum);
        }
        return connector;
    }


    private List<Position> generateHalls(Position start, TETile[][] theWorld, List<Position> deadEnds) {
        if (start.outOfTheWorld(theWorld)) {
            throw new IllegalArgumentException("invalid position (" + start.getX() + "," + start.getY() + ")");
        }

        List<Position> neighbours = start.evenNeighbours(theWorld, Tileset.NOTHING);
        if (neighbours.size() == 0) {
            deadEnds.add(start);
            return deadEnds;
        }

        for (Position neighbour : neighbours) {
            connectPositions(start, neighbour, theWorld);
            generateHalls(neighbour, theWorld, deadEnds);
        }
        return deadEnds;
    }

    private void connectPositions(Position start, Position neighbour, TETile[][] theWorld) {
        int connectorX = (start.getX() + neighbour.getX()) / 2;
        int connectorY = (start.getY() + neighbour.getY()) / 2;

        theWorld[neighbour.getX()][neighbour.getY()] = Tileset.FLOOR;
        theWorld[connectorX][connectorY] = Tileset.FLOOR;
    }

    private List<Room> generateRooms(TETile[][] theWorld, Random random) {
        List<Room> rooms = new ArrayList<>();
        int roomNums = RandomUtils.uniform(random, 8, 13);
        while (rooms.size() < roomNums) {
            Room newRoom = Room.randomRoom(random, theWorld);
            if (!newRoom.overLap(rooms)) {
                rooms.add(newRoom);
                fillWithRoomTile(newRoom, theWorld);
            }
        }
        return rooms;
    }

    private void fillWithRoomTile(Room room, TETile[][] theWorld) {
        int xStart = room.getButtonLeft().getX();
        int xEnd = room.getTopRight().getX();
        int yStart = room.getButtonLeft().getY();
        int yEnd = room.getTopRight().getY();
        for (int x = xStart; x < xEnd + 1; x++) {
            TETile[] column = theWorld[x];
            Arrays.fill(column, yStart, yEnd + 1, Tileset.ROOM);
        }
    }
}
