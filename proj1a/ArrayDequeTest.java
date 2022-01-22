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
    void addFirst() {
        deque.addFirst("three");
        deque.addFirst("two");
        deque.addFirst("one");

        deque.printDeque();
        assertEquals(3, deque.size());
    }

    @Test
    void addLast() {
        deque.addLast("one");
        deque.addLast("two");
        deque.printDeque();

        assertEquals(2, deque.size());
    }

    @Test
    void removeFirst() {
        String removeFromEmptyDeque = deque.removeFirst();
        assertNull(removeFromEmptyDeque);

        deque.addLast("one");
        deque.addLast("two");
        deque.addLast("three");

        String removed = deque.removeFirst();
        deque.printDeque();

        assertEquals(2, deque.size());
        assertEquals("one", removed);
    }

    @Test
    void removeLast() {
        String removeFromEmptyDeque = deque.removeLast();
        assertNull(removeFromEmptyDeque);

        deque.addLast("one");
        deque.addLast("two");
        deque.addLast("three");

        String removed = deque.removeLast();
        deque.printDeque();

        assertEquals(2, deque.size());
        assertEquals("three", removed);
    }

    @Test
    void size() {
        deque.addLast("one");
        deque.addLast("two");
        deque.addLast("three");
        deque.printDeque();

        assertEquals(3, deque.size());
    }

    @Test
    void get() {
        deque.addLast("one");
        deque.addLast("two");
        deque.addLast("three");
        deque.printDeque();

        String actual = deque.get(1);

        assertEquals("two", actual);
    }
}
