public class ArrayDeque<T> {

    /**
     * Creates an empty deque.
     */
    public ArrayDeque() {
        size = 0;
        front = -1;
        rear = 0;
        items = (T[]) new Object[DEFAULT_SIZE];
    }

    /**
     * Creates a deep copy of other deque.
     *
     * @param other the ArrayDeque to be copied
     */
    public ArrayDeque(ArrayDeque other) {
    }

    /**
     * Add an item to the head of deque.
     *
     * @param item the item to add
     */
    public void addFirst(T item) {
        if (isFull()) {
            resize(items.length * RESIZE_FACTOR_UPPER);
        }
        if (front == -1) {
            front = rear = 0;
        } else if (front == 0) {
            front = size - 1;
        } else front--;
        items[front] = item;
    }

    /**
     * Add an item to the end of deque.
     *
     * @param item the item to add
     */
    public void addLast(T item) {
        if (isFull()) {
            resize(items.length * RESIZE_FACTOR_UPPER);
        }
        if (front == -1) {
            front = rear = 0;
        } else {
            rear = (rear + 1) % size;
        }
        items[rear] = item;
    }

    /**
     * Remove the first item in deque
     *
     * @return null if deque is empty, the removing item otherwise
     */
    public T removeFirst() {
        if (front == -1) {
            return null;
        }
        if ()
    }

    public T removeLast() {
        return null;
    }

    /**
     * Get the item at specified index from the ArrayDeque.
     *
     * @param index zero-based index
     * @return null if no such item exists, the item at specified index otherwise
     */
    public T get(int index) {
        return null;
    }

    /**
     * Get the number of items in the ArrayDeque.
     *
     * @return size of the ArrayDeque
     */
    public int size() {
        return size;
    }

    private int size;
    private int front;
    private int rear;
    private T[] items;
    private static final int RESIZE_FACTOR_UPPER = 2;
    private static final double RESIZE_FACTOR_LOWER = 0.25;
    private static final int DEFAULT_SIZE = 8;

    private void resize(int newSize) {

    }

    private boolean isFull() {
        return ((rear + 1) % size == front);
    }

    private boolean isEmpty() {
        return front == -1;
    }
}
