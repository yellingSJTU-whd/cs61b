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
        String lowerCase = word.toLowerCase();
        Node curr = root;
        for (int i = 0; i < lowerCase.length(); i++) {
            char ch = lowerCase.charAt(i);
            int index = ch - 'a';
            if (index < 0 || index > 25) {
                continue;
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
        String lowerCase = str.toLowerCase();
        Node curr = root;
        for (int i = 0; i < lowerCase.length(); i++) {
            char ch = lowerCase.charAt(i);
            int index = ch - 'a';
            if (index < 0 || index > 25) {
                continue;
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
        String lowerCase = s.toLowerCase();
        for (int i = 0; i < s.length(); i++) {
            char ch = lowerCase.charAt(i);
            int index = ch - 'a';
            if (index < 0 || index > 25) {
                continue;
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
            children = new Node[26];
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
