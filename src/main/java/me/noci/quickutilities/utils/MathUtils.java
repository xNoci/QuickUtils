package me.noci.quickutilities.utils;

public class MathUtils {

    public static int clamp(int min, int max, int value) {
        return Math.max(min, Math.min(value, max));
    }

    public static long clamp(long min, long max, long value) {
        return Math.max(min, Math.min(value, max));
    }

    public static float clamp(float min, float max, float value) {
        return Math.max(min, Math.min(value, max));
    }

    public static double clamp(double min, double max, double value) {
        return Math.max(min, Math.min(value, max));
    }

}