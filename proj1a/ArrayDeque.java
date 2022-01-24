public class ArrayDeque<T> implements Deque<T> {

    /**
     * Creates an empty deque.
     */
    public ArrayDeque() {
        front = rear = -1;
        items = (T[]) new Object[DEFAULT_SIZE];
    }

    /**
     * Add an item to the front of deque.
     *
     * @param item the item to add
     */
    @Override
    public void addFirst(T item) {
        if (isFull()) {
            resize(items.length * RESIZE_FACTOR_UPPER);
        }
        if (front == -1) {
            front = rear = 0;
        } else if (front == 0) {
            front = items.length - 1;
        } else {
            front--;
        }
        items[front] = item;
    }

    /**
     * Add an item to the rear of deque.
     *
     * @param item the item to add
     */
    @Override
    public void addLast(T item) {
        if (isFull()) {
            resize(items.length * RESIZE_FACTOR_UPPER);
        }
        if (rear == -1) {
            front = rear = 0;
        } else if (rear == items.length - 1) {
            rear = 0;
        } else {
            rear++;
        }
        items[rear] = item;
    }

    /**
     * Remove the item at the front.
     *
     * @return null if deque was empty, the removing item otherwise
     */
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T item = items[front];
        if (front == rear) {
            front = rear = -1;
        } else if (front == items.length - 1) {
            front = 0;
        } else {
            front++;
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
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T item = items[rear];
        if (front == rear) {
            front = rear = -1;
        } else if (rear == 0) {
            rear = items.length - 1;
        } else {
            rear--;
        }
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
    @Override
    public T get(int getIndex) {
        if (getIndex < 0 || isEmpty() || items.length <= getIndex) {
            return null;
        }
        int ptr = (front + getIndex);
        if (ptr >= items.length) {
            ptr -= items.length;
        }
        return items[ptr];
    }

    /**
     * Get the number of items in deque.
     *
     * @return size of deque
     */
    @Override
    public int size() {
        if (isEmpty()) {
            return 0;
        }
        int size = rear - front + 1;
        if (size <= 0) {
            size += items.length;
        }
        return size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     */
    @Override
    public void printDeque() {
        if (isEmpty()) {
            System.out.println("deque empty");
        } else {
            int ptr = front;
            while (ptr != rear && ptr < items.length) {
                System.out.print(items[ptr++] + " ");
                if (ptr == items.length) {
                    ptr = 0;
                }
            }
            System.out.print(items[ptr]);
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
            while (ptr != rear) {
                newItems[i++] = items[ptr++];
            }
        }
        newItems[i] = items[ptr];

        front = 0;
        rear = i;
        items = newItems;
    }

    private boolean lowUsageRate() {
        return items.length >= 16 && (double) size() / items.length <= RESIZE_FACTOR_LOWER;
    }

    private boolean isFull() {
        return ((front == 0 && rear == items.length - 1) || (front == rear + 1));
    }

    /**
     * Returns whether this deque is empty.
     *
     * @return true if deque is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return front == -1;
    }
}
