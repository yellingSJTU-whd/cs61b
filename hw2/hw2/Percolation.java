package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.Arrays;

public class Percolation {

    private final WeightedQuickUnionUF weightedQuickUnionUF;
    private final int order;
    private int openSitesCount;
    private final boolean[] opened;

    /**
     * Constructor for Percolation.
     *
     * @param N dimension of the percolation system, throws illegalArgumentException if N<=0
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("should be positive: " + N);
        }

        order = N;
        openSitesCount = 0;
        opened = new boolean[order * order];
        Arrays.fill(opened, false);

        weightedQuickUnionUF = new WeightedQuickUnionUF(N * N + 2);
        for (int i = 0; i < N; i++) {
            weightedQuickUnionUF.union(i, N * N);
        }
        for (int i = N * (N - 1); i < N * N; i++) {
            weightedQuickUnionUF.union(i, N * N + 1);
        }
    }

    /**
     * Open the specified site if no already opened.
     *
     * @param row row of the site
     * @param col column of the site
     */
    public void open(int row, int col) {
        if (outOfRange(row) || outOfRange(col)) {
            throw new IndexOutOfBoundsException("Order: " + order
                    + " row: " + row + " column: " + col);
        }

        if (!isOpen(row, col)) {
            openSitesCount++;
            opened[convertTo1D(row, col)] = true;

            int idx = convertTo1D(row, col);
            if (row > 0 && isOpen(row - 1, col)) {
                weightedQuickUnionUF.union(idx, convertTo1D(row - 1, col));
            }
            if (row < order - 1 && isOpen(row + 1, col)) {
                weightedQuickUnionUF.union(idx, convertTo1D(row + 1, col));
            }
            if (col > 0 && isOpen(row, col - 1)) {
                weightedQuickUnionUF.union(idx, convertTo1D(row, col - 1));
            }
            if (col < order - 1 && isOpen(row, col + 1)) {
                weightedQuickUnionUF.union(idx, convertTo1D(row, col + 1));
            }
        }
    }

    private boolean outOfRange(int indices) {
        return indices < 0 || indices >= order;
    }

    /**
     * Return whether the specified site is open.
     *
     * @param row row of the site
     * @param col column of the site
     * @return true if open, false otherwise
     */
    public boolean isOpen(int row, int col) {
        if (outOfRange(row) || outOfRange(col)) {
            throw new IndexOutOfBoundsException("order: " + order
                    + " row: " + row + " column: " + col);
        }

        return opened[convertTo1D(row, col)];
    }

    /**
     * Return whether the specified site is full.
     *
     * @param row row of the site
     * @param col column of the site
     * @return true if full, false otherwise.
     */
    public boolean isFull(int row, int col) {
        if (outOfRange(row) || outOfRange(col)) {
            throw new IndexOutOfBoundsException("Order: " + order
                    + " row: " + row + " column: " + col);
        }

        return isOpen(row, col)
                && weightedQuickUnionUF.connected(convertTo1D(row, col), order * order);
    }

    /**
     * Fetch the total number of open sites.
     *
     * @return the number of open sites
     */
    public int numberOfOpenSites() {
        return openSitesCount;
    }

    /**
     * Return whether the percolation system percolates.
     *
     * @return true if system percolates, false otherwise
     */
    public boolean percolates() {
        return weightedQuickUnionUF.connected(order * order, order * order + 1);
    }

    public static void main(String[] args) {
    }

    private int convertTo1D(int row, int col) {
        return row * order + col;
    }
}
