import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void gradeScoped01() {
        for (int i = 0; i < 100; i++) {
            deque.addFirst("nonsense");
        }
        for (int i = 0; i < 100; i++) {
            deque.removeFirst();
        }
        System.out.println(deque.size());
//        deque.printDeque();
    }
}
