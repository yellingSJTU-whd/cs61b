import edu.princeton.cs.algs4.Picture;

import java.awt.*;

public class SeamCarver {

    private final Picture picture;

    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
    }

    public Picture picture() {
        return new Picture(picture);
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public double energy(int x, int y) {
        validateColumnIndex(x);
        validateRowIndex(y);

        Color preX = picture.get(x == 0 ? width() - 1 : x - 1, y);
        Color postX = picture.get(x == width() - 1 ? 0 : x + 1, y);
        Color preY = picture.get(x, y == 0 ? height() - 1 : y - 1);
        Color postY = picture.get(x, y == height() - 1 ? 0 : y + 1);

        return energyHelper(preX, postX) + energyHelper(preY, postY);
    }

    private void validateColumnIndex(int col) {
        if (col < 0 || col >= width()) {
            throw new IllegalArgumentException("column index must be between 0 and "
                    + (width() - 1) + ": " + col);
        }
    }

    private void validateRowIndex(int row) {
        if (row < 0 || row >= height()) {
            throw new IllegalArgumentException("row index must be between 0 and "
                    + (height() - 1) + ": " + row);
        }
    }

    private double energyHelper(Color pre, Color post) {
        double deltaRed = pre.getRed() - post.getRed();
        double deltaGreen = pre.getGreen() - post.getGreen();
        double deltaBlue = pre.getBlue() - post.getBlue();
        return deltaRed * deltaRed + deltaGreen * deltaGreen + deltaBlue * deltaBlue;
    }

    public int[] findHorizontalSeam() {
        return null;
    }

    public int[] findVerticalSeam() {
        int[][] preIndex = new int[width()][height()];

        double[] minCostLastRow = new double[width()];
        for (int i = 0; i < width(); i++) {
            minCostLastRow[i] = energy(0, i);
        }

        double[] minCostThisRow = new double[width()];
        for (int y = 1; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                double energy = energy(x, y);
                double top = minCostLastRow[x];
                double topLeft, topRight;
                if (x == 0) {
                    topRight = minCostLastRow[x + 1];
                    if (top <= topRight) {
                        preIndex[x][y] = x;
                        minCostThisRow[x] = energy + top;
                    } else {
                        preIndex[x][y] = x + 1;
                        minCostThisRow[x] = energy + topRight;
                    }
                } else if (x == width() - 1) {
                    topLeft = minCostLastRow[x - 1];
                    if (top <= topLeft) {
                        preIndex[x][y] = x;
                        minCostThisRow[x] = energy + top;
                    } else {
                        preIndex[x][y] = x - 1;
                        minCostThisRow[x] = energy + topLeft;
                    }
                } else {
                    topLeft = minCostLastRow[x - 1];
                    topRight = minCostLastRow[x + 1];
                    if (top <= topLeft && top <= topRight) {
                        preIndex[x][y] = x;
                        minCostThisRow[x] = energy + top;
                    } else if (topLeft <= top && topLeft <= topRight) {
                        preIndex[x][y] = x - 1;
                        minCostThisRow[x] = energy + topLeft;
                    } else {
                        preIndex[x][y] = x + 1;
                        minCostThisRow[x] = energy + topRight;
                    }
                }
            }
            minCostLastRow = minCostThisRow;
        }

        int minIndex = -1;
        double minEnergySum = 0.0;
        for (int i = 0; i < width(); i++) {

        }
    }

    public void removeHorizontalSeam(int[] seam) {
    }

    public void removeVerticalSeam(int[] seam) {
    }
}
