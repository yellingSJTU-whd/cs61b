package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private final int period;
    private int state;

    public SawToothGenerator(int period) {
        this.period = period;
        state = 0;
    }

    @Override
    public double next() {
        state++;
        return normalize();
    }

    private double normalize() {
        return -1.0 + (state % period) / (double) (period - 1) * 2;
    }
}
