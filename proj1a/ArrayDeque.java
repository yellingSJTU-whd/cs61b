public class ArrayDeque<T> {

    /**
     * Creates an empty deque.
     */
    public ArrayDeque() {
        front = rear = 0;
        items = (T[]) new Object[DEFAULT_SIZE];
    }

    /**
     * Add an item to the front of deque.
     *
     * @param item the item to add
     */
    public void addFirst(T item) {
        if (isFull()) {
            resize(items.length * RESIZE_FACTOR_UPPER);
        }
        front--;
        if (front < 0) {
            front += items.length;
        }
        items[front] = item;
    }

    /**
     * Add an item to the rear of deque.
     *
     * @param item the item to add
     */
    public void addLast(T item) {
        if (isFull()) {
            resize(items.length * RESIZE_FACTOR_UPPER);
        }

        items[rear] = item;
        rear++;
        if (rear > items.length) {
            rear -= items.length;
        }
    }

    /**
     * Remove the item at the front.
     *
     * @return null if deque was empty, the removing item otherwise
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T item = items[front];
        front++;
        if (front > items.length) {
            front -= items.length;
        }
        if (lowUsageRate()) {
            resize((int) (items.length * 0.5));
        }
        return item;
    }

    /**
     * Remove the item at the rear.
     *
     * @return null if deque was empty, the removing item otherwise
     */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        rear--;
        if (rear < 0) {
            rear += items.length;
        }
        T item = items[rear];
        if (lowUsageRate()) {
            resize((int) (items.length * 0.5));
        }
        return item;
    }

    /**
     * Get the item at specified index in deque.
     *
     * @param getIndex zero-based index
     * @return null if no such item exists, the item otherwise
     */
    public T get(int getIndex) {
        if (getIndex < 0 || isEmpty() || size() - 1 < getIndex) {
            return null;
        }
        int ptr = (front + getIndex) % items.length;
        return items[ptr];
    }

    /**
     * Get the number of items in deque.
     *
     * @return size of deque
     */
    public int size() {
        int size = rear - front;
        while (size < 0) {
            size += items.length;
        }
        return size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     */
    public void printDeque() {
        if (isEmpty()) {
            System.out.println("deque empty");
        } else {
            int ptr = front;
            while (ptr != rear) {
                System.out.print(items[ptr] + " ");
                ptr = (ptr + 1) % items.length;
            }
        }
        System.out.println();
    }

    private int front;
    private int rear;
    private T[] items;
    private static final int RESIZE_FACTOR_UPPER = 2;
    private static final double RESIZE_FACTOR_LOWER = 0.25;
    private static final int DEFAULT_SIZE = 8;

    private void resize(int newSize) {
        T[] newItems = (T[]) new Object[newSize];

        int ptr = front;
        int i = 0;
        while (!(ptr == rear || ptr == items.length)) {
            newItems[i++] = items[ptr++];
        }
        if (ptr == items.length && ptr != rear) {
            ptr = 0;
            while (ptr != front) {
                newItems[i++] = items[ptr++];
            }
        }

        front = 0;
        rear = items.length;
        items = newItems;
    }

    private boolean lowUsageRate() {
        return items.length >= 16 && (double) size() / items.length < RESIZE_FACTOR_LOWER;
    }

    private boolean isFull() {
        return size() == items.length;
    }

    /**
     * Returns whether this deque is empty.
     *
     * @return true if deque is empty, false otherwise
     */
    public boolean isEmpty() {
        return front == rear;
    }
}
