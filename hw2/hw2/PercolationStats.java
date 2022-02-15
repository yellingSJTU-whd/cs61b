package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.Stopwatch;

import java.util.ArrayList;
import java.util.List;

public class PercolationStats {
    private double mean, stddev, confidenceLow, confidenceHigh;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("must be positive: N = " + N + ", T = " + T);
        }

        Stopwatch timer = new Stopwatch();
        List<Double> samples = evalMean(N, T, pf);
        evalStddev(samples, T);
        evalConfidenceInterval(T);
        System.out.println("total time in seconds: " + timer.elapsedTime());
    }

    private void evalConfidenceInterval(int T) {
        double delta = 1.96 * stddev / (Math.sqrt(T));
        confidenceLow = mean - delta;
        confidenceHigh = mean + delta;
    }

    private void evalStddev(List<Double> samples, int T) {
        double deviation = 0;
        for (Double sample : samples) {
            deviation += Math.pow(sample - mean, 2);
        }
        deviation /= (T - 1);
        stddev = Math.sqrt(deviation);
    }

    private List<Double> evalMean(int N, int T, PercolationFactory pf) {
        List<Double> samples = new ArrayList<>(T);

        for (int i = 0; i < T; i++) {
            Percolation system = pf.make(N);
            while (!system.percolates()) {
                int randomRow = StdRandom.uniform(N);
                int randomCol = StdRandom.uniform(N);
                system.open(randomRow, randomCol);
            }
            mean += system.numberOfOpenSites();
            samples.add(mean);
        }
        mean = mean / (N * N) / T;
        return samples;
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLow() {
        return confidenceLow;
    }

    public double confidenceHigh() {
        return confidenceHigh;
    }
}
