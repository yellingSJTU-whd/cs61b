import java.util.Arrays;
import java.util.Objects;

public class Trie {

    private final Node root;

    /**
     * Construct a trie using an array of Strings.
     */
    public Trie(String[] dict) {
        root = new Node();
        Arrays.stream(dict).forEach(this::insert);
    }

    /**
     * Inner method to search the corresponding node, according to input String.
     */
    private Node search(String key) {
        Node curr = root;
        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            if ((ch < 'a' || ch > 'z') && (ch < 'A' || ch > 'Z')) {
                return null;
            }
            int index = ch - 'a' < 0 ? ch - 'A' + 26 : ch - 'a';
            curr = curr.children[index];
            if (curr == null) {
                return null;
            }
        }
        return curr;
    }

    public boolean contained(String word) {
        Objects.requireNonNull(word, "NULL");
        Node node = search(word);
        return node != null;
    }

    public boolean isWord(String str) {
        Objects.requireNonNull(str);
        Node node = search(str);
        return node != null && node.isWord;
    }

    private void insert(String s) {
        Node curr = root;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if ((ch < 'a' || ch > 'z') && (ch < 'A' || ch > 'Z')) {
                return;
            }
            int index = ch - 'a';
            if (index < 0) {
                index = ch - 'A' + 26;
            }
            curr.setChild(index);
            curr = curr.children[index];
        }
        curr.setWord();
    }

    private static class Node {
        boolean isWord;
        Node[] children;

        private Node() {
            isWord = false;
            children = new Node[52];
        }

        private void setWord() {
            isWord = true;
        }

        private void setChild(int index) {
            if (children[index] == null) {
                children[index] = new Node();
            }
        }
    }
}
