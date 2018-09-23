package pl.karczmarczyk.utils;

/**
 *
 * @author mateusz
 */
public class PlotUtils {
    public static int calcField (int x1, int z1, int x2, int z2) {
        int a = Math.abs(x1-x2);
        int b = Math.abs(z1-z2);
        return a*b;
    }
}
