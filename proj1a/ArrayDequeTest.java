import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ArrayDequeTest {

    ArrayDeque<Integer> deque;

    @BeforeAll
    static void initAll() {
    }

    @BeforeEach
    void init() {
        deque = new ArrayDeque<>();
    }

    @Test
    void d011_ad_basic_get(){
        deque.addLast(4);
        deque.addFirst(5);
        deque.addLast(6);
        deque.addLast(7);

        deque.addLast(9);
        deque.printDeque();
        deque.addFirst(11);
        deque.addFirst(12);
        deque.addFirst(13);
        deque.printDeque();

        deque.addLast(14);
        deque.printDeque();
        System.out.println("remove last ==> " +  deque.removeLast());
        deque.printDeque();
        System.out.println("remove first ==> " +  deque.removeFirst());
    }
}
