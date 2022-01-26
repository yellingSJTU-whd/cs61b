package synthesizer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the ArrayRingBuffer class.
 *
 * @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void simpleTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<Integer>(3);
        assertTrue(arb.isEmpty());
        assertEquals(0, arb.fillCount());

        arb.enqueue(0);
        assertEquals(1, arb.fillCount());

        arb.enqueue(1);
        arb.enqueue(2);
        assertTrue(arb.isFull());
        assertEquals(Integer.valueOf(0), arb.dequeue());
    }

    /**
     * Calls tests for ArrayRingBuffer.
     */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
