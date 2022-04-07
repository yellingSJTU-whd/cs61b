package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private final int period;
    private int state;

    public StrangeBitwiseGenerator(int period) {
        this.period = period;
        state = 0;
    }

    @Override
    public double next() {
        state++;
        int weirdState = state & (state >>> 3) % period;
//        int weirdState = state & (state >> 3) & (state >> 8) % period;
        return normalize(weirdState);
    }

    private double normalize(double weirdState) {
        return -1.0 + (weirdState % period) / (double) (period - 1) * 2;
    }
}
