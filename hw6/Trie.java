import java.util.Arrays;

public class Trie {

    private Node root;

    public Trie(String[] dict) {
        root = new Node();
        Arrays.stream(dict).forEach(this::add);
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
            Node child = children[index];
            if (child == null) {
                child = new Node();
            }
        }
    }
}
