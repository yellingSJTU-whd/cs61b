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
     * @param seed pseudo-random seed
     * @return the world generated
     */
    private TETile[][] generateWorld(long seed){
        //1. init
        TETile[][] theWorld = new TETile[WIDTH][HEIGHT];
        for (TETile[] column:theWorld){
            Arrays.fill(column, Tileset.NOTHING);
        }
        //2. generate rooms pseudo-randomly
        Random random = new Random(seed);
        List<Room> rooms = generateRooms(theWorld, random);






        return theWorld;
    }

    private List<Room> generateRooms(TETile[][] theWorld, Random random) {
        List<Room> rooms = new ArrayList<>();
        int roomNums = RandomUtils.uniform(random, 8,13);
        while (rooms.size()<roomNums){

        }



        return rooms;
    }
}
