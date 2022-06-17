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
                    search(queue, session, trie);
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

    private static void bfs(String[] board, int x, int y, MinPQ<String> heap,
                            StringBuffer stringBuffer, boolean[][] visited, Trie trie) {
        int height = board.length;
        int width = board[0].length();
        char curr = board[y].charAt(x);
        if (visited[y][x]) {
            return;
        }
        boolean[][] reposition = copyMatrix(visited);
        reposition[y][x] = true;
        StringBuffer sb = new StringBuffer(stringBuffer).append(curr);
        if (!trie.contained(stringBuffer.toString())) {
            return;
        }

        if (trie.isWord(sb.toString())) {
            heap.insert(sb.toString());
        }

        if (x > 0) {
            bfs(board, x - 1, y, heap, sb, reposition, trie);
        }
        if (x < width - 1) {
            bfs(board, x + 1, y, heap, sb, reposition, trie);
        }
        if (y > 0) {
            bfs(board, x, y - 1, heap, sb, reposition, trie);
        }
        if (y < height - 1) {
            bfs(board, x, y + 1, heap, sb, reposition, trie);
        }
        if (x > 0 && y > 0) {
            bfs(board, x - 1, y - 1, heap, sb, reposition, trie);
        }
        if (x > 0 && y < height - 1) {
            bfs(board, x - 1, y + 1, heap, sb, reposition, trie);
        }
        if (x < width - 1 && y > 0) {
            bfs(board, x + 1, y - 1, heap, sb, reposition, trie);
        }
        if (x < width - 1 && y < height - 1) {
            bfs(board, x + 1, y + 1, heap, sb, reposition, trie);
        }
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

        char ch = board[session.row].charAt(session.col);
        String str = session.preSequence.append(ch).toString();
        if (trie.isWord(str)) {
            heap.insert(str);
        }
    }

    private static void search(Queue<Session> queue, Session session, Trie trie) {
        boolean[][] visited = session.visited;
        visited[session.row][session.col] = true;
    }
}
