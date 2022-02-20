package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;


public class Board implements WorldState {
    private static final int BLANK = 0;
    private final int width;
    private final int[] tiles;

    /**
     * Constructs a board from an N-by-N array of tiles where
     * tiles[i][j] = tile at row i, column j, while 0 stands for blank.
     *
     * @param tiles a matrix represents a board
     */
    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException("tiles can't be null!");
        }
        width = tiles.length;
        int height = tiles[0].length;
        if (width != height) {
            throw new IllegalArgumentException("input matrix must be square");
        }
        this.tiles = new int[width * height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int tile = tiles[x][y];
                if (tile < 0 || tile > width * height - 1) {
                    throw new IndexOutOfBoundsException("Invalid tile number: " + tile);
                }
                this.tiles[x * width + y] = tile;
            }
        }

    }

    /**
     * Returns value of tile at row i, column j (or 0 if blank)
     *
     * @param i row
     * @param j column
     * @return value at the specified position if presented, 0 otherwise
     */
    public int tileAt(int i, int j) {
        return this.tiles[i * width + j];
    }

    /**
     * @return the board size N
     */
    private int size() {
        return width;
    }

    /**
     * @return the neighbors of the current board
     * @source http://joshh.ug/neighbors.html
     */
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    /**
     * Hamming estimate
     *
     * @return hamming estimation of distance to goal
     */
    public int hamming() {
        int hammingCount = 0;
        for (int i = 1; i < tiles.length; i++) {
            if (tiles[i] != i) {
                hammingCount++;
            }
        }
        return hammingCount;
    }

    /**
     * Manhattan estimate
     *
     * @return manhattan estimation of distance to goal
     */
    public int manhattan() {
        int manhattanCount = 0;
        for (int i = 0; i < tiles.length; i++) {
            int row = i / width;
            int col = i % width;
            int tile = tiles[i];
            int refRow = tile / width;
            int refCol = tile % width - 1;
            manhattanCount += Math.abs(row - refRow) + Math.abs(col - refCol);
        }
        return manhattanCount;
    }


    /**
     * @return Estimated distance to goal
     */
    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    /**
     * @param y object to be tested on equality
     * @return true if this board's tile values are the same
     * position as y's, false otherwise
     */
    @Override
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board that = (Board) y;
        return Arrays.equals(tiles, that.tiles);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(tiles);
    }

    /**
     * Returns the string representation of the board.
     * Uncomment this method.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N).append("\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
}
