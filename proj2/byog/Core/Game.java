package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 81;
    public static final int HEIGHT = 31;
    private static String operations = "";
    private static Player player;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {

        //1. show the starting UI
        showMainMenu();

        //2. wait for and read input(only N、L、Q is valid)
        String beginningStr = solicitBeginningStr();

        if (beginningStr.equals("N")) {
            newGame();
        } else if (beginningStr.equals("L")) {
            loadAndPlay();
        } else {
            saveOperations();
            System.exit(0);
        }
    }

    private void newGame() {
        operations = "";
        operations += "N";

        promptToSeedUi();
        String seed = solicitSeed();
        operations = operations + seed + "S";

        TETile[][] theWorld = generateWorld(Long.parseLong(seed));
        StdDraw.clear(Color.BLACK);
        ter.renderFrame(theWorld);

        interactWith(theWorld);
    }

    private void loadAndPlay() {
        operations = loadOperations();
        if (operations.length() == 0) {
            throw new RuntimeException("load error");
        }
        TETile[][] theWorld = playWithInputString(operations);
        StdDraw.clear(Color.BLACK);
        ter.renderFrame(theWorld);
        interactWith(theWorld);
    }

    private void renderHUD(TETile[][] theWorld, String gameInfo) {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        String description = "";
        if (!new Position(x, y).outOf(theWorld)) {
            description = theWorld[x][y].description();
        }

        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 20));
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.textLeft(0, HEIGHT + 1, description);
        StdDraw.textLeft(WIDTH / 2.0 - 2, HEIGHT + 1, gameInfo);
        StdDraw.show();
    }

    private void interactWith(TETile[][] theWorld) {
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char input = Character.toUpperCase(StdDraw.nextKeyTyped());
            switch (input) {
                case 'W':
                    if (player.moveNorth(theWorld)) {
                        operations += "W";
                        reDraw(theWorld, "went north");
                    } else {
                        reDraw(theWorld, "can't go north");
                    }
                    break;
                case 'A':
                    if (player.moveWest(theWorld)) {
                        operations += "A";
                        reDraw(theWorld, "went west");
                    } else {
                        reDraw(theWorld, "can't go west");
                    }
                    break;
                case 'S':
                    if (player.moveSouth(theWorld)) {
                        operations += "S";
                        reDraw(theWorld, "went south");
                    } else {
                        reDraw(theWorld, "can't go south");
                    }
                    break;
                case 'D':
                    if (player.moveEast(theWorld)) {
                        operations += "D";
                        reDraw(theWorld, "went east");
                    } else {
                        reDraw(theWorld, "can't go east");
                    }
                    break;
                case ':':
                    while (true) {
                        if (!StdDraw.hasNextKeyTyped()) {
                            renderHUD(theWorld, "waiting for keyboard input");
                            continue;
                        }
                        char ch = Character.toUpperCase(StdDraw.nextKeyTyped());
                        if (ch == 'Q') {
                            renderHUD(theWorld, "saving...");
                            saveOperations();
                            System.exit(0);
                        }
                        break;
                    }
                    break;
                default:
            }
        }
    }

    private void reDraw(TETile[][] theWorld, String gameInfo) {
        StdDraw.clear(Color.BLACK);
        ter.renderFrame(theWorld);
        renderHUD(theWorld, gameInfo);
    }

    private Long parseSeed(String operations) {
        String upper = operations.toUpperCase();
        int indexOfN = upper.indexOf("N");
        int indexOfS = upper.indexOf("S");
        return Long.parseLong(upper.substring(indexOfN + 1, indexOfS));
    }

    private String loadOperations() {
        File f = new File("./world.ser");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                String loadOperations = (String) os.readObject();
                os.close();
                return loadOperations;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        return "";
    }

    private void saveOperations() {
        File f = new File("./world.ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(operations.toString());
            os.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private String solicitSeed() {
        char endFlag = 'S';
        StringBuilder seedBuilder = new StringBuilder();
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char input = StdDraw.nextKeyTyped();
            if (Character.isDigit(input)) {
                seedBuilder.append(input);
                StdDraw.clear(Color.BLACK);
                setGameTitle();
                StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
                StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0 + 1, seedBuilder.toString());
                StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0 - 1, "Please entry a random number");
                StdDraw.show();
            } else if (Character.toUpperCase(input) == endFlag) {
                return seedBuilder.toString();
            }
        }
    }

    private void promptToSeedUi() {
        StdDraw.clear(Color.BLACK);
        setGameTitle();
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0 - 1, "Please entry a random number");
        StdDraw.show();
    }

    private String solicitBeginningStr() {
        StringBuilder beginningStr = new StringBuilder();
        while (beginningStr.length() != 1) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char input = Character.toUpperCase(StdDraw.nextKeyTyped());
            if (input == 'N' || input == 'L' || input == 'Q') {
                beginningStr.append(input);
            }
        }
        return beginningStr.toString();
    }

    private void showMainMenu() {
        initCanvas();
        setGameTitle();

        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0 + 2, "New Game (N)");
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0, "Load Game (L)");
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0 - 2, "Quit (Q)");

        StdDraw.show();
    }

    private void setGameTitle() {
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 60));
        StdDraw.text(WIDTH / 2.0, HEIGHT * 3 / 4.0, "CS61B:  THE GAME");
    }

    private void initCanvas() {
//        StdDraw.setCanvasSize(WIDTH * 16, (HEIGHT + 2) * 16);
        ter.initialize(WIDTH, HEIGHT + 2);
        StdDraw.setPenColor(Color.WHITE);
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
        String upper = input.toUpperCase();
        int delimitation = upper.indexOf("S") + 1;
        int quitIndex = upper.indexOf(":Q");
        if (upper.startsWith("N")) {
            long seed = parseSeed(input);
            finalWorldFrame = generateWorld(seed);
            if (quitIndex >= 0) {
                processMovementStr(finalWorldFrame, upper.substring(delimitation, quitIndex));
                saveOperations();
                System.exit(0);
            } else {
                processMovementStr(finalWorldFrame, upper.substring(delimitation));
            }
        } else if (upper.startsWith("L")) {
            operations = loadOperations();
            finalWorldFrame = playWithInputString(operations);
            if (quitIndex >= 0) {
                processMovementStr(finalWorldFrame, upper.substring(1, quitIndex));
            } else {
                processMovementStr(finalWorldFrame, upper.substring(1));
            }
        } else {
            throw new IllegalStateException("illegal input: " + input);
        }
        return finalWorldFrame;
    }

    private void processMovementStr(TETile[][] theWorld, String movement) {
        if (movement.length() == 0) {
            return;
        }
        String gameInfo = "";
        switch (movement.substring(0, 1)) {
            case "W":
                if (player.moveNorth(theWorld)) {
                    operations += "W";
                    reDraw(theWorld, "went north");
                } else {
                    reDraw(theWorld, "can't go north");
                }
                break;
            case "A":
                if (player.moveWest(theWorld)) {
                    operations += "A";
                    reDraw(theWorld, "went west");
                } else {
                    reDraw(theWorld, "can't go west");
                }
                break;
            case "S":
                if (player.moveSouth(theWorld)) {
                    operations += "S";
                    reDraw(theWorld, "went south");
                } else {
                    reDraw(theWorld, "can't go south");
                }
                break;
            case "D":
                if (player.moveEast(theWorld)) {
                    operations += "D";
                    reDraw(theWorld, "went east");
                } else {
                    reDraw(theWorld, "can't go east");
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + movement.charAt(0));
        }
        processMovementStr(theWorld, movement.substring(1));
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
        for (int x = 0; x < theWorld.length; x++) {
            for (int y = 0; y < theWorld[0].length; y++) {
                if (isWall(theWorld, x, y)) {
                    theWorld[x][y] = Tileset.WALL;
                }
            }
        }

        //7. set beginning position
        int luckyNum = RandomUtils.uniform(random, newEnds.size());
        Position beginning = newEnds.get(luckyNum);
        theWorld[beginning.getX()][beginning.getY()] = Tileset.LOCKED_DOOR;
        player = new Player(beginning);

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
            if (RandomUtils.bernoulli(random, 0.6)) {
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
                Position candidate = new Position(x, y);
                if (!candidate.outOf(theWorld) && theWorld[x][y].equals(Tileset.FLOOR)) {
                    candidates.add(candidate);
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
        if (start.outOf(theWorld)) {
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
