package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int period;
    private int state;
    private final double factor;

    public AcceleratingSawToothGenerator(int initialPeriod, double accFactor) {
        period = initialPeriod;
        factor = accFactor;
        state = 0;
    }

    @Override
    public double next() {
        state++;
        if (state >= period) {
            state = 0;
            period = (int) (period * (factor));
        }
        return normalize();
    }

    private double normalize() {
        return -1.0 + (state % period) / (double) (period - 1) * 2;
    }
}
