import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BinaryTrie implements Serializable {

    private final Node root;

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        MinPQ<Node> heap = new MinPQ<>(frequencyTable.size());
        for (Character ch : frequencyTable.keySet()) {
            Integer freq = frequencyTable.get(ch);
            Node node = new Node(ch, freq, null, null);
            heap.insert(node);
        }

        while (heap.size() > 1) {
            Node left = heap.delMin();
            Node right = heap.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            heap.insert(parent);
        }
        root = heap.delMin();
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        Node curr = root;
        BitSequence bs = new BitSequence();
        for (int i = 0; i < querySequence.length(); i++) {
            int bit = querySequence.bitAt(i);
            bs = bs.appended(bit);
            if (bit == 0) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }

            if (curr.isLeaf()) {
                return new Match(bs, curr.ch);
            }
        }
        return new Match(querySequence, null);
    }

    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> map = new HashMap<>();
        return DFS(root, new BitSequence(), map);
    }

    private Map<Character, BitSequence> DFS(Node node, BitSequence bs, Map<Character, BitSequence> map) {
        if (node.isLeaf()) {
            map.put(node.ch, bs);
            return map;
        }
        DFS(node.left, bs.appended(0), map);
        DFS(node.right, bs.appended(1), map);
        return map;
    }

    private static class Node implements Comparable<Node>, Serializable {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(Node that) {
            return freq - that.freq;
        }

        private boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return left == null;
        }
    }
}
