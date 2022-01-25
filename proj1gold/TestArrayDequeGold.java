import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestArrayDequeGold {
    StudentArrayDeque<Integer> sad;
    ArrayDequeSolution<Integer> ads;

    @Before
    public void init() {
        sad = new StudentArrayDeque<>();
        ads = new ArrayDequeSolution<>();
    }

    @Test
    public void randomizedTest() {
        for (int i = 0; i < 500; i++) {
            int random = StdRandom.uniform(4);
            switch (random) {
                case 0:
                    sad.addFirst(random);
                    ads.addFirst(random);
                    assertEquals("Oh nooo!\nThis is bad:\n    size of StudentArrayDeque = " + sad.size() +
                            ", while size of ArrayDequeSolution = " + ads.size(), ads.size(), sad.size());
                    break;

                case 1:
                    sad.addLast(random);
                    sad.addLast(random);
                    assertEquals("Oh nooo!\nThis is bad:\n    size of StudentArrayDeque = " + sad.size() +
                            ", while size of ArrayDequeSolution = " + ads.size(), ads.size(), sad.size());
                    break;

                case 2:
                    assertEquals("Oh nooo!\nThis is bad:\n    size of StudentArrayDeque = " + sad.size() +
                            ", while size of ArrayDequeSolution = " + ads.size(), ads.size(), sad.size());
                    if (ads.size() > 0) {
                        Integer student = sad.removeFirst();
                        Integer ref = ads.removeFirst();
                        assertEquals("Oh nooo!\nThis is bad:\n    removeFirst() of StudentArrayDeque returns"
                                + student + ", while removeFirst() of ArrayDequeSolution returns " + ref, ref, student);
                    }
                    break;

                case 3:
                    assertEquals("Oh nooo!\nThis is bad:\n    size of StudentArrayDeque = " + sad.size() +
                            " while size of ArrayDequeSolution = " + ads.size(), ads.size(), sad.size());
                    if (ads.size() > 0) {
                        Integer student = sad.removeLast();
                        Integer ref = ads.removeLast();
                        assertEquals("Oh nooo!\nThis is bad:\n    removeLast() of StudentArrayDeque returns"
                                + student + ", while removeLast() of ArrayDequeSolution returns " + ref, ref, student);
                    }
                    break;
            }
        }
    }
}
