/**
 * Deque is an irregular acronym of double-ended queue.
 *
 * @param <T> type of items in the deque.
 */
public class LinkedListDeque<T> {

    /**
     * Constructor for LinkedListDeque.
     */
    public LinkedListDeque() {
        size = 0;
        sentinel = new Node((T) new Object(), null, null);
        sentinel.pre = sentinel;
        sentinel.next = sentinel;
    }

    /**
     * Adds an item of type T to the head of the deque.
     *
     * @param item the item to be added
     */
    public void addFirst(T item) {
        Node first = new Node(item, sentinel, sentinel.next);
        sentinel.next.pre = first;
        sentinel.next = first;
        size++;
    }

    /**
     * Adds an item of type T to the end of the deque.
     *
     * @param item the item to be added
     */
    public void addLast(T item) {
        Node last = new Node(item, sentinel.pre, sentinel);
        sentinel.pre.next = last;
        sentinel.pre = last;
        size++;
    }

    /**
     * Returns true if deque is empty, false otherwise.
     *
     * @return true if deque is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of items in the deque.
     *
     * @return the number of items in the deque
     */
    public int size() {
        return size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     */
    public void printDeque() {
        Node ptr = sentinel;
        while (ptr.next != sentinel) {
            ptr = ptr.next;
            System.out.print(ptr.item + " ");
        }
        System.out.println();
    }

    /**
     * Removes and returns the item at the front of the deque. If no such item exists, returns null.
     *
     * @return null if no such item exists, the item to be removed otherwise
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Node first = sentinel.next;
        first.next.pre = sentinel;
        sentinel.next = first.next;
        size--;
        return first.item;
    }

    /**
     * Removes and returns the item at the back of the deque. If no such item exists, returns null.
     *
     * @return null if no such item exists, the item to be removed otherwise
     */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        Node last = sentinel.pre;
        last.pre.next = sentinel;
        sentinel.pre = last.pre;
        size--;
        return last.item;
    }

    /**
     * Gets the item at the given index.
     *
     * @param index zero-based index
     * @return null if no such item exists, the item at specified index otherwise
     */
    public T get(int index) {
        if (size < index) {
            return null;
        }
        Node ptr = sentinel.next;
        while (index-- > 0) {
            ptr = ptr.next;
        }
        return ptr.item;
    }

    /**
     * Gets the item at the given index recursively.
     *
     * @param index zero-based index
     * @return null if no such item exists, the item at specified index otherwise
     */
    public T getRecursive(int index) {
        if (size < index) {
            return null;
        }
        return getRecursive(index, sentinel.next);
    }

    private T getRecursive(int index, Node curr) {
        if (index == 0) {
            return curr.item;
        }
        return getRecursive(index - 1, curr.next);
    }

    private int size;
    private Node sentinel;

    private class Node {
        private T item;
        private Node pre;
        private Node next;

        public Node(T item, Node pre, Node next) {
            this.item = item;
            this.pre = pre;
            this.next = next;
        }
    }
}
