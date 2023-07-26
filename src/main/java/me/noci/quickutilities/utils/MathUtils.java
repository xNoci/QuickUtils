package me.noci.quickutilities.utils;

public class MathUtils {

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    public static long clamp(long value, long min, long max) {
        return Math.max(min, Math.min(value, max));
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    //https://stackoverflow.com/a/3305400
    public static int log2(int x) {
        return (int) (Math.log(x) / Math.log(2) + 1e-10);
    }


}
