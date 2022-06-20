import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Boggle {

    // File path of dictionary file
    static String dictPath = "words.txt";

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
        File file = new File(dictPath);
        if (!file.exists()) {
            throw new IllegalArgumentException("The dictionary file does not exist: " + dictPath);
        }

        In in = new In(dictPath);
        String[] dict = in.readAllStrings();
        MinPQ<String> heap = new MinPQ<>(k, Comparator.comparingInt(String::length).
                reversed().thenComparing(String::compareToIgnoreCase));
        Trie trie = new Trie(dict);

        in = new In(boardFilePath);
        String[] board = in.readAllStrings();
        int height = board.length;
        int width = board[0].length();
        Queue<Session> queue = new Queue<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                queue.enqueue(new Session(x, y, new StringBuilder(), new boolean[height][width]));
                while (!queue.isEmpty()) {
                    Session session = queue.dequeue();
                    apply(session, heap, board, trie);
                    search(queue, session, trie, board);
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

    public static boolean[][] copyMatrix(boolean[][] original) {
        int a = original.length;
        int b = original[0].length;
        boolean[][] copied = new boolean[a][b];
        for (int i = 0; i < a; i++) {
            copied[i] = Arrays.copyOf(original[i], b);
        }
        return copied;
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String boardPath = "smallBoard.txt";
        List<String> solution = solve(7, boardPath);
        System.out.println(solution);
        long end = System.currentTimeMillis();
        System.out.println("time elapsed :" + (end - start));
    }

    private static class Session {
        int col;
        int row;
        StringBuilder preSequence;
        boolean[][] visited;

        private Session(int col, int row, StringBuilder preSequence, boolean[][] visited) {
            this.col = col;
            this.row = row;
            this.preSequence = preSequence;
            this.visited = visited;
        }
    }

    private static void apply(Session session, MinPQ<String> heap, String[] board, Trie trie) {
        int x = session.col;
        int y = session.row;
        char ch = board[y].charAt(x);
        String str = session.preSequence.append(ch).toString();
        if (trie.isWord(str)) {
            heap.insert(str);
        }
        session.visited[y][x] = true;
    }

    private static void search(Queue<Session> queue, Session session, Trie trie, String[] board) {
        boolean[][] visited = session.visited;
        StringBuilder sb = session.preSequence;
        int x = session.col;
        int y = session.row;
        int height = board.length;
        int width = board[0].length();
        visited[y][x] = true;

        check(x > 0, visited, y, x - 1, trie, sb, board, queue);
        check(x < width - 1, visited, y, x + 1, trie, sb, board, queue);
        check(y > 0, visited, y - 1, x, trie, sb, board, queue);
        check(y < height - 1, visited, y + 1, x, trie, sb, board, queue);
        check(x > 0 && y > 0, visited, y - 1, x - 1, trie, sb, board, queue);
        check(x > 0 && y < height - 1, visited, y + 1, x - 1, trie, sb, board, queue);
        check(x < width - 1 && y > 0, visited, y - 1, x + 1, trie, sb, board, queue);
        check(x < width - 1 && y < height - 1, visited, y + 1, x + 1, trie, sb, board, queue);
    }

    private static void check(boolean x, boolean[][] visited, int y, int x1,
                              Trie trie, StringBuilder sb, String[] board, Queue<Session> queue) {
        if (x && !visited[y][x1]) {
            if (trie.contained(sb.toString() + board[y].charAt(x1))) {
                queue.enqueue(new Session(x1, y, new StringBuilder(sb), copyMatrix(visited)));
            }
        }
    }

}
