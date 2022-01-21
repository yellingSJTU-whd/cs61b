import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ArrayDequeTest {

    ArrayDeque<String> deque;

    @BeforeAll
    static void initAll() {
    }

    @BeforeEach
    void init() {
        deque = new ArrayDeque<>();
    }

    @Test
    void addFront() {
        deque.addFront("three");
        deque.addFront("two");
        deque.addFront("one");

        deque.printDeque();
        assertEquals(3, deque.size());
    }

    @Test
    void addRear() {
        deque.addRear("one");
        deque.addRear("two");
        deque.printDeque();

        assertEquals(2, deque.size());
    }

    @Test
    void removeFront() {
        String removeFromEmptyDeque = deque.removeFront();
        assertNull(removeFromEmptyDeque);

        deque.addRear("one");
        deque.addRear("two");
        deque.addRear("three");

        String removed = deque.removeFront();
        deque.printDeque();

        assertEquals(2, deque.size());
        assertEquals("one", removed);
    }

    @Test
    void removeRear() {
        String removeFromEmptyDeque = deque.removeRear();
        assertNull(removeFromEmptyDeque);

        deque.addRear("one");
        deque.addRear("two");
        deque.addRear("three");

        String removed = deque.removeRear();
        deque.printDeque();

        assertEquals(2, deque.size());
        assertEquals("three", removed);
    }

    @Test
    void getFront() {
        String getFromEmpty = deque.getFront();
        assertNull(getFromEmpty);

        deque.addRear("one");
        deque.addRear("two");
        deque.addRear("three");
        deque.printDeque();

        String front = deque.getFront();

        assertEquals("one", front);
    }

    @Test
    void getRear() {
        String getFromEmpty = deque.getRear();
        assertNull(getFromEmpty);

        deque.addRear("one");
        deque.addRear("two");
        deque.addRear("three");
        deque.printDeque();

        String rear = deque.getRear();

        assertEquals("three", rear);
    }

    @Test
    void size() {
        deque.addRear("one");
        deque.addRear("two");
        deque.addRear("three");
        deque.printDeque();

        assertEquals(3, deque.size());
    }
}
