import java.util.Arrays;
import java.util.Objects;

public class Trie {

    private Node root;

    public Trie(String[] dict) {
        root = new Node();
        Arrays.stream(dict).forEach(this::add);
    }

    public boolean contained(String word) {
        Objects.requireNonNull(word, "NULL");
        Node curr = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            int index = ch - 'a';
            if (index < 0) {
                index = ch - 'A' + 26;
            }
            curr = curr.children[index];
            if (curr == null) {
                return false;
            }
        }
        return true;
    }

    public boolean isWord(String str) {
        Objects.requireNonNull(str);
        Node curr = root;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            int index = ch - 'a';
            if (index < 0) {
                index = ch - 'A' + 26;
            }
            curr = curr.children[index];
            if (curr == null) {
                return false;
            }
        }
        return curr.isWord;
    }

    private void add(String s) {
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
