import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
                    assertEquals(sb.toString(), ads.size(), sad.size());
                    assertEquals(sb.toString(), ads.get(0), sad.get(0));
                    break;

                case 1:
                    sad.addLast(i);
                    sad.addLast(i);
                    sb.append("addLast(").append(i).append(")\n");
                    assertEquals(sb.toString(), ads.size(), sad.size());
                    assertEquals(sb.toString(), ads.get(ads.size() - 1), sad.get(sad.size() - 1));
                    break;

                case 2:
                    assertEquals(sb.toString(), ads.size(), sad.size());
                    if (ads.size() > 0) {
                        Integer student = sad.removeFirst();
                        Integer ref = ads.removeFirst();
                        sb.append("removeFirst()\n");
                        assertEquals(sb.toString(), ads.size(), sad.size());
                        assertEquals(sb.toString(), ref, student);
                    }
                    break;

                case 3:
                    assertEquals(sb.toString(), ads.size(), sad.size());
                    if (ads.size() > 0) {
                        Integer student = sad.removeLast();
                        Integer ref = ads.removeLast();
                        sb.append("removeLast()\n");
                        assertEquals(sb.toString(), ads.size(), sad.size());
                        assertEquals(sb.toString(), ref, student);
                    }
                    break;
            }
        }
    }
}
