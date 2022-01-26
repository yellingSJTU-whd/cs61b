package synthesizer;

public interface BoundedQueue<T> {
    /**
     * Return size of the buffer.
     *
     * @return size of the buffer
     */
    int capacity();

    /**
     * Return number of items currently in the buffer.
     *
     * @return number of items in the buffer
     */
    int fillCount();

    /**
     * Add item x to the end.
     *
     * @param x the item to add
     */
    void enqueue(T x);

    /**
     * Delete and return item from the front.
     *
     * @return the item deleted
     */
    T dequeue();

    /**
     * Return (but do not delete) item from the front.
     *
     * @return the item at the front
     */
    T peek();

    /**
     * Return true if the buffer is empty, false otherwise.
     *
     * @return true if the buffer is empty, false otherwise
     */
    default boolean isEmpty() {
        return fillCount() == 0;
    }

    /**
     * Return true if the buffer is full, false otherwise.
     *
     * @return true if the buffer is full, false otherwise
     */
    default boolean isFull() {
        return fillCount() == capacity();
    }
}
