import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

import java.util.Comparator;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class Boggle {

    // File path of dictionary file
    static String dictPath = "words.txt";
    static Trie trie;
    static String board;
    static int height;
    static int width;
    static Queue<Session> queue;

    /**
     * Solves a Boggle puzzle.
     *
     * @param k             The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     * The Strings are sorted in descending order of length.
     * If multiple words have the same length,
     * have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        if (k < 1) {
            throw new IllegalArgumentException("non-positive parameter: " + k);
        }
        In in = new In(dictPath);
        if (!in.exists()) {
            throw new IllegalArgumentException("Dictionary file does not exist: " + dictPath);
        }
        String[] dict = in.readAllStrings();
        MinPQ<String> heap = new MinPQ<>(k, Comparator.comparingInt(String::length).
                reversed().thenComparing(String::compareToIgnoreCase));
        trie = new Trie(dict);
        in = new In(boardFilePath);
        String[] lines = in.readAllStrings();
        board = String.join("", lines);
        height = lines.length;
        width = lines[0].length();
        queue = new Queue<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                queue.enqueue(new Session(x, y, new StringBuilder(), new boolean[width * height]));
                while (!queue.isEmpty()) {
                    Session session = queue.dequeue();
                    apply(session, heap);
                    search(session);
                }
            }
        }

        List<String> solution = new ArrayList<>(k);
        while (solution.size() < k && heap.size() > 0) {
            String str = heap.delMin();
            if (solution.contains(str)) {
                continue;
            }
            solution.add(str);
        }
        return solution;
    }

//    public static void main(String[] args) {
//        long start = System.currentTimeMillis();
//        String boardPath = "smallBoard.txt";
//        List<String> solution = solve(10, boardPath);
//        System.out.println(solution);
//        long end = System.currentTimeMillis();
//    }

    private static class Session {
        int col;
        int row;
        StringBuilder preSequence;
        boolean[] visited;

        private Session(int col, int row, StringBuilder preSequence, boolean[] visited) {
            this.col = col;
            this.row = row;
            this.preSequence = new StringBuilder(preSequence);
            this.visited = Arrays.copyOf(visited, width * height);
        }
    }

    private static void apply(Session session, MinPQ<String> heap) {
        int x = session.col;
        int y = session.row;
        char ch = board.charAt(y * width + x);
        String str = session.preSequence.append(ch).toString();
        if (trie.isWord(str)) {
            heap.insert(str);
        }
        session.visited[y * width + x] = true;
    }

    private static void search(Session session) {
        boolean[] visited = session.visited;
        StringBuilder sb = session.preSequence;
        int x = session.col;
        int y = session.row;

        check(x > 0, visited, y, x - 1, sb);
        check(x < width - 1, visited, y, x + 1, sb);
        check(y > 0, visited, y - 1, x, sb);
        check(y < height - 1, visited, y + 1, x, sb);
        check(x > 0 && y > 0, visited, y - 1, x - 1, sb);
        check(x > 0 && y < height - 1, visited, y + 1, x - 1, sb);
        check(x < width - 1 && y > 0, visited, y - 1, x + 1, sb);
        check(x < width - 1 && y < height - 1, visited, y + 1, x + 1, sb);
    }

    private static void check(boolean posChecker, boolean[] visited, int row, int col,
                              StringBuilder sb) {
        if (!posChecker || visited[row * width + col]) {
            return;
        }
        if (trie.contained(sb.toString() + board.charAt(row * width + col))) {
            queue.enqueue(new Session(col, row, sb, visited));
        }
    }
}
