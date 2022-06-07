import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {

    private Picture picture;

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
        Picture invertPic = new Picture(height(), width());
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                invertPic.set(row, col, picture.get(col, row));
            }
        }
        Picture save = picture;
        picture = invertPic;
        int[] horizontalSeam = findVerticalSeam();
        picture = save;
        return horizontalSeam;
    }

    public int[] findVerticalSeam() {
        if (width() == 1) {
            return new int[height()];
        }
        int[][] preIndex = new int[width()][height()];

        double[] minCostLastRow = new double[width()];

        int minForSingleRow = -1;
        double min = Double.MAX_VALUE;
        for (int x = 0; x < width(); x++) {
            double e = energy(x, 0);
            if (e < min) {
                minForSingleRow = x;
            }
            minCostLastRow[x] = e;
        }
        if (height() == 1) {
            return new int[]{minForSingleRow};
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
            minCostLastRow = Arrays.copyOf(minCostThisRow, width());
        }

        int minIndex = -1;
        double minEnergySum = Double.MAX_VALUE;
        for (int i = 0; i < width(); i++) {
            if (minCostThisRow[i] < minEnergySum) {
                minEnergySum = minCostThisRow[i];
                minIndex = i;
            }
        }

        int[] verticalSeam = new int[height()];
        verticalSeam[height() - 1] = minIndex;
        for (int y = height() - 1; y > 0; y--) {
            int lastRowIndex = preIndex[minIndex][y];
            verticalSeam[y - 1] = lastRowIndex;
            minIndex = lastRowIndex;
        }
        return verticalSeam;
    }

    public void removeHorizontalSeam(int[] seam) {
        Picture afterRemoval = new Picture(width(), height() - 1);
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height() - 1; row++) {
                if (row != seam[col]) {
                    afterRemoval.set(col, row, picture.get(col, row));
                }
            }
        }
        picture = afterRemoval;
    }

    public void removeVerticalSeam(int[] seam) {
        Picture afterRemoval = new Picture(width() - 1, height());
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                if (col != seam[row]) {
                    afterRemoval.set(col, row, picture.get(col, row));
                }
            }
        }
        picture = afterRemoval;
    }
}
