import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestArrayDequeGold {
    StudentArrayDeque<Integer> sad;
    ArrayDequeSolution<Integer> ads;
    StringBuilder sb = new StringBuilder();

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
                    sad.addFirst(i);
                    ads.addFirst(i);
                    sb.append("addFirst(").append(i).append(")\n");
                    assertEquals(sb.toString(), ads.get(0), sad.get(0));

                    sb.append("size()\n");
                    assertEquals(sb.toString(), ads.size(), sad.size());
                    break;

                case 1:
                    sad.addLast(i);
                    sad.addLast(i);
                    sb.append("addLast(").append(i).append(")\n");
                    assertEquals(sb.toString(), ads.get(ads.size() - 1), sad.get(sad.size() - 1));

                    sb.append("size()\n");
                    assertEquals(sb.toString(), ads.size(), sad.size());
                    break;

                case 2:
                    if (ads.isEmpty()) {
                        sb.append("isEmpty()\n");
                        assertTrue(sb.toString(), sad.isEmpty());
                    } else {
                        Integer student = sad.removeFirst();
                        Integer ref = ads.removeFirst();
                        sb.append("removeFirst()\n");
                        assertEquals(sb.toString(), ref, student);

                        sb.append("size()\n");
                        assertEquals(sb.toString(), ads.size(), sad.size());
                    }
                    break;

                case 3:
                    if (ads.isEmpty()) {
                        sb.append("isEmpty()\n");
                        assertTrue(sb.toString(), sad.isEmpty());
                    } else {
                        Integer student = sad.removeLast();
                        Integer ref = ads.removeLast();
                        sb.append("removeLast()\n");
                        assertEquals(sb.toString(), ref, student);

                        sb.append("size()\n");
                        assertEquals(sb.toString(), ads.size(), sad.size());
                    }
                    break;

                default:
            }
        }
    }
}
