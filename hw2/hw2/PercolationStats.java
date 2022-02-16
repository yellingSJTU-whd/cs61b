package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;
import edu.princeton.cs.introcs.Stopwatch;

public class PercolationStats {
    private final int T;
    private final double[] fractions;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("must be positive: N = " + N + ", T = " + T);
        }

        Stopwatch timer = new Stopwatch();
        this.T = T;
        fractions = new double[T];

        for (int i = 0; i < T; i++) {
            Percolation system = pf.make(N);
            while (!system.percolates()) {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                system.open(row, col);
            }
            fractions[i] = (double) system.numberOfOpenSites() / (N * N);
        }

        System.out.println("total time in seconds: " + timer.elapsedTime());
    }

    public double mean() {
        return StdStats.mean(fractions);
    }

    public double stddev() {
        return StdStats.stddev(fractions);
    }

    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }
}
