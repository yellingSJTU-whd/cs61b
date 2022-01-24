import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void gradeScoped01() {
        deque.addLast(0);
        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);
        deque.addLast(4);
        deque.addLast(5);
        deque.addLast(6);
        deque.addLast(7);
        deque.printDeque();
        System.out.println(deque.get(7));
    }
}
