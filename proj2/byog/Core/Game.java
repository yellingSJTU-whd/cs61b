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
    private String operations;
    private Player player;
    private Random random;
    private TETile[][] theWorld;

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
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 14));
        ter.renderFrame(theWorld);
        StdDraw.setPenColor(Color.ORANGE);
        StdDraw.line(0, HEIGHT, WIDTH, HEIGHT);
        StdDraw.show();

        interact();
    }

    private void loadAndPlay() {
        operations = loadOperations();
        if (operations.length() == 0) {
            throw new RuntimeException("load error");
        }
        TETile[][] theWorld = playWithInputString(operations);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 14));
        ter.renderFrame(theWorld);
        interact();
    }

    private void renderHUD(String gameInfo) {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        String description = "";
        if (!new Position(x, y).outOf(theWorld)) {
            description = theWorld[x][y].description();
        }

        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 25));
        StdDraw.setPenColor(Color.ORANGE);

        StdDraw.textLeft(0, HEIGHT + 1, description);
        StdDraw.text(WIDTH / 2.0, HEIGHT + 1, gameInfo);

        StdDraw.line(0, HEIGHT, WIDTH, HEIGHT);

        StdDraw.show();
    }

    private void interact() {
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char input = Character.toUpperCase(StdDraw.nextKeyTyped());
            switch (input) {
                case 'W':
                    if (player.moveNorth(theWorld)) {
                        operations += "W";
                        reDraw("went north");
                    } else {
                        reDraw("can't go north");
                    }
                    break;
                case 'A':
                    if (player.moveWest(theWorld)) {
                        operations += "A";
                        reDraw("went west");
                    } else {
                        reDraw("can't go west");
                    }
                    break;
                case 'S':
                    if (player.moveSouth(theWorld)) {
                        operations += "S";
                        reDraw("went south");
                    } else {
                        reDraw("can't go south");
                    }
                    break;
                case 'D':
                    if (player.moveEast(theWorld)) {
                        operations += "D";
                        reDraw("went east");
                    } else {
                        reDraw("can't go east");
                    }
                    break;
                case ':':
                    while (true) {
                        if (!StdDraw.hasNextKeyTyped()) {
                            renderHUD("waiting for keyboard input");
                            continue;
                        }
                        char ch = Character.toUpperCase(StdDraw.nextKeyTyped());
                        if (ch == 'Q') {
                            renderHUD("saving...");
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

    private void reDraw(String gameInfo) {
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 14));
        ter.renderFrame(theWorld);
        renderHUD(gameInfo);
    }

    private Long parseSeed(String interactions) {
        String upper = interactions.toUpperCase();
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
        setGameOptions();

        StdDraw.show();
    }

    private void setGameOptions() {
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0 + 2, "New Game (N)");
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0, "Load Game (L)");
        StdDraw.text(WIDTH / 2.0, HEIGHT / 2.0 - 2, "Quit (Q)");
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

        String upper = input.toUpperCase();
        int delimitation = upper.indexOf("S") + 1;
        int quitIndex = upper.indexOf(":Q");
        if (upper.startsWith("N")) {
            long seed = parseSeed(input);
            theWorld = generateWorld(seed);
            if (quitIndex >= 0) {
                processMovementStr(upper.substring(delimitation, quitIndex));
                saveOperations();
                System.exit(0);
            } else {
                processMovementStr(upper.substring(delimitation));
            }
        } else if (upper.startsWith("L")) {
            operations = loadOperations();
            theWorld = playWithInputString(operations);
            if (quitIndex >= 0) {
                processMovementStr(upper.substring(1, quitIndex));
            } else {
                processMovementStr(upper.substring(1));
            }
        } else {
            throw new IllegalStateException("illegal input: " + input);
        }
        return theWorld;
    }

    private void processMovementStr(String movement) {
        if (movement.length() == 0) {
            return;
        }
        String gameInfo = "";
        switch (movement.substring(0, 1)) {
            case "W":
                if (player.moveNorth(theWorld)) {
                    operations += "W";
                    reDraw("went north");
                } else {
                    reDraw("can't go north");
                }
                break;
            case "A":
                if (player.moveWest(theWorld)) {
                    operations += "A";
                    reDraw("went west");
                } else {
                    reDraw("can't go west");
                }
                break;
            case "S":
                if (player.moveSouth(theWorld)) {
                    operations += "S";
                    reDraw("went south");
                } else {
                    reDraw("can't go south");
                }
                break;
            case "D":
                if (player.moveEast(theWorld)) {
                    operations += "D";
                    reDraw("went east");
                } else {
                    reDraw("can't go east");
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + movement.charAt(0));
        }
        processMovementStr(movement.substring(1));
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
        theWorld = new TETile[WIDTH][HEIGHT];
        for (TETile[] column : theWorld) {
            Arrays.fill(column, Tileset.NOTHING);
        }
        random = new Random(seed);

        //2. generate rooms pseudo-randomly
        List<Room> rooms = generateRooms();

        //3. generate halls from button left of the map
        Position start = new Position(1, 1);
        theWorld[1][1] = Tileset.FLOOR;
        List<Position> deadEnds = generateHalls(start, new ArrayList<>(), new boolean[WIDTH][HEIGHT]);
//
        //4. connect rooms and halls
        connectRoomsAndHalls(rooms);

        //5. subtract deadEnds pseudo-randomly
        List<Position> newEnds = removeDeadEnds(deadEnds, new ArrayList<>(deadEnds.size()));
//
//        //6. generate walls
//        generateWalls();
//
//        //7. set beginning position
//        int luckyNum = RandomUtils.uniform(random, newEnds.size());
//        Position beginning = newEnds.get(luckyNum);
//        theWorld[beginning.getX()][beginning.getY()] = Tileset.LOCKED_DOOR;
//        player = new Player(beginning);

        return theWorld;
    }

    private void generateWalls() {
        for (int x = 0; x < theWorld.length; x++) {
            for (int y = 0; y < theWorld[0].length; y++) {
                if (isWall(x, y)) {
                    theWorld[x][y] = TETile.colorVariant(Tileset.WALL, 30, 30, 30, random);
                }
            }
        }
    }

    private boolean isWall(int x, int y) {
        if (!theWorld[x][y].equals(Tileset.NOTHING)) {
            return false;
        }
        Position current = new Position(x, y);
        List<Position> neighbourFloorTiles = current.diagonalNeighbours(theWorld, Tileset.FLOOR);
        List<Position> neighbourRoomTiles = current.diagonalNeighbours(theWorld, Tileset.ROOM);
        return neighbourFloorTiles.size() + neighbourRoomTiles.size() > 0;
    }

    private List<Position> removeDeadEnds(List<Position> deadEnds, List<Position> newEnds) {
        if (deadEnds == null || deadEnds.size() == 0) {
            return newEnds;
        }
        for (Position deadEnd : deadEnds) {
            List<Position> neighbours = deadEnd.oddNeighbours(theWorld, Tileset.FLOOR);
            if (RandomUtils.bernoulli(random, 0.75)) {
                theWorld[deadEnd.getX()][deadEnd.getY()] = Tileset.NOTHING;
                removeDeadEnds(neighbours, newEnds);
            } else if (deadEnd.oddNeighbours(theWorld, Tileset.FLOOR).size() != 0) {
                theWorld[deadEnd.getX()][deadEnd.getY()] = Tileset.DEADEND;
                newEnds.add(deadEnd);
            } else {
                theWorld[deadEnd.getX()][deadEnd.getY()] = Tileset.NOTHING;
            }
        }
        return newEnds;
    }


    private void connectRoomsAndHalls(List<Room> rooms) {
        for (Room room : rooms) {
            connectRoomsAndHalls(room);
        }
    }

    private void connectRoomsAndHalls(Room room) {
        Position buttonRight = new Position(room.getTopRight().getX(), room.getButtonLeft().getY());
        Position topLeft = new Position(room.getButtonLeft().getX(), room.getTopRight().getY());

        connectRoomsAndHalls(room.getButtonLeft(), buttonRight, Direction.DOWN);
        connectRoomsAndHalls(room.getButtonLeft(), topLeft, Direction.LEFT);
        connectRoomsAndHalls(topLeft, room.getTopRight(), Direction.UP);
        connectRoomsAndHalls(buttonRight, room.getTopRight(), Direction.RIGHT);
    }

    private void connectRoomsAndHalls(Position start, Position end, Direction direction) {
        List<Position> candidates = new ArrayList<>(4);
        Position connector, candidate;

        if (direction.equals(Direction.DOWN) || direction.equals(Direction.UP)) {
            int y = direction.equals(Direction.DOWN) ? start.getY() - 2 : start.getY() + 2;
            for (int x = start.getX(); x <= end.getX(); x++) {
                candidate = new Position(x, y);
                if (!candidate.outOf(theWorld) && theWorld[x][y].equals(Tileset.FLOOR)) {
                    candidates.add(candidate);
                }
            }

            connector = connector(candidates);
            if (connector != null) {
                int yPrim = y < start.getY() ? y + 1 : y - 1;
                theWorld[connector.getX()][yPrim] = TETile.colorVariant(Tileset.FLOOR, 30, 30, 30, random);
            }
        } else {
            int connectorX = direction.equals(Direction.LEFT) ? start.getX() - 2 : start.getX() + 2;
            for (int y = start.getY(); y <= end.getY(); y++) {
                candidate = new Position(connectorX, y);
                if (!candidate.outOf(theWorld) && theWorld[connectorX][y].equals(Tileset.FLOOR)) {
                    candidates.add(candidate);
                }
            }

            connector = connector(candidates);
            if (connector != null) {
                int bridgeX = connectorX < start.getX() ? connectorX + 1 : connectorX - 1;
                theWorld[bridgeX][connector.getY()] = TETile.colorVariant(Tileset.FLOOR, 30, 30, 30, random);
            }
        }
    }

    private Position connector(List<Position> candidates) {
        Position connector = null;
        if (candidates.size() != 0 && RandomUtils.bernoulli(random, 0.7)) {
            int luckyNum = RandomUtils.uniform(random, candidates.size());
            connector = candidates.get(luckyNum);
        }
        return connector;
    }


    private List<Position> generateHalls(Position start, List<Position> deadEnds, boolean[][] visited) {
        if (start.outOf(theWorld) || visited[start.getX()][start.getY()]) {
            return deadEnds;
        }
        visited[start.getX()][start.getY()] = true;
        List<Position> neighbours = start.evenNeighbours(theWorld, Tileset.NOTHING);

        if (neighbours.size() == 0) {
            deadEnds.add(start);
            return deadEnds;
        }

        while (neighbours.size() > 0) {
            int randomIndex = RandomUtils.uniform(random, neighbours.size());
            Position neighbour = neighbours.remove(randomIndex);
            if (!visited[neighbour.getX()][neighbour.getY()]) {
                connectPositions(start, neighbour);
            }
            generateHalls(neighbour, deadEnds, visited);
        }

        return deadEnds;
    }

    private void connectPositions(Position start, Position neighbour) {
        int connectorX = (start.getX() + neighbour.getX()) / 2;
        int connectorY = (start.getY() + neighbour.getY()) / 2;

        theWorld[neighbour.getX()][neighbour.getY()] = TETile.colorVariant(Tileset.FLOOR, 30, 30, 30, random);
        theWorld[connectorX][connectorY] = TETile.colorVariant(Tileset.FLOOR, 30, 30, 30, random);
    }

    private List<Room> generateRooms() {
        List<Room> rooms = new ArrayList<>();
        int roomNums = RandomUtils.uniform(random, 10, 20);
        while (rooms.size() < roomNums) {
            Room newRoom = Room.randomRoom(random, theWorld);
            if (!newRoom.overLap(rooms) && !newRoom.adjacent(rooms)) {
                rooms.add(newRoom);
                fillWithRoomTile(newRoom);
            }
        }
        return rooms;
    }

    private void fillWithRoomTile(Room room) {
        int xStart = room.getButtonLeft().getX();
        int xEnd = room.getTopRight().getX();
        int yStart = room.getButtonLeft().getY();
        int yEnd = room.getTopRight().getY();

        for (int x = xStart; x < xEnd + 1; x++) {
            TETile[] column = theWorld[x];
            Arrays.fill(column, yStart, yEnd + 1, TETile.colorVariant(Tileset.ROOM, 30, 30, 30, random));
        }
    }
}
