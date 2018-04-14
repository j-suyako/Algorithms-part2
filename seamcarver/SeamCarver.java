
import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture currPic;
    private int width;
    private int height;
    private double[][] currEnergy;

    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        width = picture.width();
        height = picture.height();
        currPic = new Picture(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++)
                currPic.setRGB(x, y, picture.getRGB(x, y));
        }
        currEnergy = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++)
                currEnergy[x][y] = energy(x, y);
        }
    }

    public Picture picture() {
        Picture res = new Picture(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++)
                res.setRGB(x, y, currPic.getRGB(x, y));
        }
        return res;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    private double deltaX(int x1, int x2, int y) {
        Color rgb_left = currPic.get(x1, y);
        Color rgb_right = currPic.get(x2, y);
        return Math.pow(rgb_left.getRed() - rgb_right.getRed(), 2) +
                Math.pow(rgb_left.getGreen() - rgb_right.getGreen(), 2) +
                Math.pow(rgb_left.getBlue() - rgb_right.getBlue(), 2);
    }

    private double deltaY(int x, int y1, int y2) {
        Color rgb_up = currPic.get(x, y1);
        Color rgb_down = currPic.get(x, y2);
        return Math.pow(rgb_down.getRed() - rgb_up.getRed(), 2) +
                Math.pow(rgb_down.getGreen() - rgb_up.getGreen(), 2) +
                Math.pow(rgb_down.getBlue() - rgb_up.getBlue(), 2);
    }

    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) throw new IllegalArgumentException();
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
            return 1000;
        return Math.sqrt(deltaX(x - 1, x + 1, y) + deltaY(x, y - 1, y + 1));
    }

    private void horizontalRelax(int x, int y, double[] distTo, int[] edgeTo) {
        if (x < 0) throw new IllegalArgumentException();
        if (x == width - 1) {
            if (distTo[width * height] > distTo[x * height + y]) {
                distTo[width * height] = distTo[x * height + y];
                edgeTo[width * height] = x * height + y;
            }
            return;
        }
        int[] adj = {-1, 0, 1};
        if (y == 0) {
            adj[0] = 0;
        }
        if (y == height - 1) {
            adj[2] = 0;
        }
        for (int e : adj) {
            if (distTo[(x + 1) * height + y + e] > distTo[x * height + y] + currEnergy[x + 1][y + e]) {
                distTo[(x + 1) * height + y + e] = distTo[x * height + y] + currEnergy[x + 1][y + e];
                edgeTo[(x + 1) * height + y + e] = x * height + y;
            }
        }
    }

    public int[] findHorizontalSeam() {
        int[] res = new int[width];
        double[] distTo = new double[width * height + 1];
        int[] edgeTo = new int[width * height + 1];
        for (int i = 0; i < distTo.length; i++) {
            if (i < height) {
                distTo[i] = 1000;
                edgeTo[i] = -1;
            } else {
                distTo[i] = Double.POSITIVE_INFINITY;
                edgeTo[i] = i;
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++)
                horizontalRelax(x, y, distTo, edgeTo);
        }
        int curr = width * height;
        int currWidth = width;
        while (edgeTo[curr] != -1) {
            curr = edgeTo[curr];
            res[--currWidth] = curr % height;
        }
        return res;
    }

    private void verticalRelax(int x, int y, double[] distTo, int[] edgeTo) {
        if (y < 0) throw new IllegalArgumentException();
        if (y == height - 1) {
            if (distTo[width * height] > distTo[x * height + y]) {
                distTo[width * height] = distTo[x * height + y];
                edgeTo[width * height] = x * height + y;
            }
            return;
        }
        int[] adj = {-1, 0, 1};
        if (x == 0) {
            adj[0] = 0;
        }
        if (x == width - 1) {
            adj[2] = 0;
        }
        for (int e : adj) {
            if (distTo[(x + e) * height + y + 1] > distTo[x * height + y] + currEnergy[x + e][y + 1]) {
                distTo[(x + e) * height + y + 1] = distTo[x * height + y] + currEnergy[x + e][y + 1];
                edgeTo[(x + e) * height + y + 1] = x * height + y;
            }
        }
    }

    public int[] findVerticalSeam() {
        int[] res = new int[height];
        double[] distTo = new double[width * height + 1];
        int[] edgeTo = new int[width * height + 1];
        for (int i = 0; i < distTo.length; i++) {
            if (i % height == 0 && i != width * height) {
                distTo[i] = 1000;
                edgeTo[i] = -1;
            } else {
                distTo[i] = Double.POSITIVE_INFINITY;
                edgeTo[i] = i;
            }
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                verticalRelax(x, y, distTo, edgeTo);
        }
        int curr = width * height;
        int currheight = height;
        while (edgeTo[curr] != -1) {
            curr = edgeTo[curr];
            res[--currheight] = Math.floorDiv(curr, height);
        }
        return res;
    }

    private boolean check(int[] seam, int length) {
        if (seam.length != length) return false;
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i + 1] - seam[i]) > 1)
                return false;
        }
        return true;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (height <= 1) throw new IllegalArgumentException();
        if(!check(seam, width)) throw new IllegalArgumentException();
        Picture newPic = new Picture(width, height - 1);
        double[][] newEnergy = new double[width][height - 1];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (y < seam[x])
                    newPic.setRGB(x, y, currPic.getRGB(x, y));
                else if (y > seam[x])
                    newPic.setRGB(x, y - 1, currPic.getRGB(x, y));
            }
        }
        currPic = newPic;
        height--;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (y < seam[x] - 1) newEnergy[x][y] = currEnergy[x][y];
                else if (y == seam[x] - 1 || y == seam[x])
                    newEnergy[x][y] = energy(x, y);
                else
                    newEnergy[x][y] = currEnergy[x][y + 1];
            }
        }
        currEnergy = newEnergy;
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (width <= 1) throw new IllegalArgumentException();
        if(!check(seam, height)) throw new IllegalArgumentException();
        Picture newPic = new Picture(width - 1, height);
        double[][] newEnergy = new double[width - 1][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x < seam[y])
                    newPic.setRGB(x, y, currPic.getRGB(x, y));
                else if (x > seam[y])
                    newPic.setRGB(x - 1, y, currPic.getRGB(x, y));
            }
        }
        currPic = newPic;
        width--;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x < seam[y] - 1) newEnergy[x][y] = currEnergy[x][y];
                else if (x == seam[y] - 1 || x == seam[y])
                    newEnergy[x][y] = energy(x, y);
                else
                    newEnergy[x][y] = currEnergy[x + 1][y];
            }
        }
        currEnergy = newEnergy;

    }
}
