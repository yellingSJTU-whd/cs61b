import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOf;

public class ArrayDeque<T> {

    /**
     * Creates an empty deque.
     */
    public ArrayDeque() {
        size = 0;
        front = -1;
        rear = -1;
        items = (T[]) new Object[DEFAULT_SIZE];
    }

    /**
     * Creates a deep copy of other deque.
     *
     * @param other the ArrayDeque to be copied
     */
    public ArrayDeque(ArrayDeque other) {
        size = other.size;
        front = other.front;
        rear = other.rear;
        T[] items = (T[]) copyOf(other.items, other.items.length);

    }

    /**
     * Add an item to the front of deque.
     *
     * @param item the item to add
     */
    public void addFront(T item) {
        if (isFull()) {
            resize(items.length * RESIZE_FACTOR_UPPER);
        }
        if (front == -1) {
            front = rear = 0;
        } else if (front == 0) {
            front = size - 1;
        } else {
            front--;
        }
        items[front] = item;
        size++;
    }

    /**
     * Add an item to the rear of deque.
     *
     * @param item the item to add
     */
    public void addRear(T item) {
        if (isFull()) {
            resize(items.length * RESIZE_FACTOR_UPPER);
        }
        if (rear == -1) {
            front = rear = 0;
        } else {
            rear = (rear + 1) % size;
        }
        items[rear] = item;
        size++;
    }

    /**
     * Remove the item at the front.
     *
     * @return null if deque was empty, the removing item otherwise
     */
    public T removeFront() {
        if (isEmpty()) {
            return null;
        }
        T item = items[front];
        if (front == rear) {
            front = rear = -1;
        } else {
            front = (front + 1) % size;
        }
        size--;
        if ((double) size / items.length < RESIZE_FACTOR_LOWER) {
            resize((int) (items.length * 0.5));
        }
        return item;
    }

    /**
     * Remove the item at the rear.
     *
     * @return null if deque was empty, the removing item otherwise
     */
    public T removeRear() {
        if (isEmpty()) {
            return null;
        }
        T item = items[rear];
        if (front == rear) {
            front = rear = -1;
        } else if (rear == 0) {
            rear = size - 1;
        } else {
            rear--;
        }
        size--;
        if ((double) size / items.length < RESIZE_FACTOR_LOWER) {
            resize((int) (items.length * 0.5));
        }
        return item;
    }

    /**
     * Get the item at the front.
     *
     * @return null if deque was empty, the item at the front of deque otherwise
     */
    public T getFront() {
        if (isEmpty()) {
            return null;
        } else {
            return items[front];
        }
    }

    /**
     * Get the item at the rear.
     *
     * @return null if deque was empty, the item at the rear of deque otherwise
     */
    public T getRear() {
        if (isEmpty()) {
            return null;
        } else {
            return items[rear];
        }
    }

    /**
     * Get the number of items in deque.
     *
     * @return size of deque
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
        T[] newItems = (T[]) new Object[newSize];
        arraycopy(items, 0, newItems, 0, items.length);
        items = newItems;
    }

    private boolean isFull() {
        return ((rear + 1) % size == front);
    }

    private boolean isEmpty() {
        return front == -1;
    }
}
