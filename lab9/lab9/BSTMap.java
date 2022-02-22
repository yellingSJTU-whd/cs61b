package lab9;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private final K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns the value mapped to by KEY in the subtree rooted in P.
     * or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        int compare = key.compareTo(p.key);
        if (compare < 0) {
            return getHelper(key, p.left);
        } else if (compare > 0) {
            return getHelper(key, p.right);
        } else {
            return p.value;
        }
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with a null key");
        }
        return getHelper(key, root);
    }

    /**
     * Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
     * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size++;
            return new Node(key, value);
        }
        int compare = key.compareTo(p.key);
        if (compare < 0) {
            p.left = putHelper(key, value, p.left);
        } else if (compare > 0) {
            p.right = putHelper(key, value, p.right);
        } else {
            p.value = value;
            size++;
        }
        return p;
    }

    /**
     * Inserts the key KEY
     * If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with a null key");
        }
        if (value == null) {
            remove(key);
            return;
        }
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keySet = new LinkedHashSet<>();
        inorderTraversal(root, keySet);
        return keySet;
    }

    private void inorderTraversal(Node node, Set<K> set) {
        if (node == null) {
            return;
        }
        if (node.left != null) {
            inorderTraversal(node.left, set);
        }
        set.add(node.key);
        if (node.right != null) {
            inorderTraversal(node.right, set);
        }
    }

    private V removeHelper(Node node, K key) {
        if (node == null) {
            return null;
        }
        int compare = key.compareTo(node.key);
        if (compare < 0) {
            return removeHelper(node.left, key);
        } else if (compare > 0) {
            return removeHelper(node.right, key);
        } else {
            if (node.left == null) {
                node = node.right;
            }
            if (node.right == null) {
                node = node.left;
            }
            Node t = node;
            node = min(t.right);
            V removed = node.value;
            node.right = removeMin(t.right);
            node.left = t.left;
            return removed;
        }
    }

    /**
     * Removes KEY from the tree if present
     * returns VALUE removed,
     * null on failed removal.
     */
    @Override
    public V remove(K key) {
        if (key == null) {
            return null;
        }
        return removeHelper(root, key);
    }

    /**
     * Removes the key-value entry for the specified key only if it is
     * currently mapped to the specified value.  Returns the VALUE removed,
     * null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        if (key == null || value == null) {
            return null;
        }
        if (!get(key).equals(value)) {
            return null;
        }
        return remove(key);
    }

    public void removeMin() {
        if (root == null) {
            throw new NoSuchElementException("underflow!");
        }
        root = removeMin(root);
    }

    private Node removeMin(Node node) {
        if (node == null) {
            return null;
        }
        if (node.left == null) {
            size--;
            return node.right;
        }
        node.left = removeMin(node.left);
        return node;
    }

    private Node min(Node node) {
        if (node.left == null) {
            return node;
        }
        return min(node.left);
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
