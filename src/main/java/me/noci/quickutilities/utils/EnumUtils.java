package me.noci.quickutilities.utils;

public class EnumUtils {

    public static <T extends Enum<T>> T next(T value) {
        int index = value.ordinal();
        T[] values = (T[]) value.getClass().getEnumConstants();

        if (index == values.length - 1) return values[0];
        return values[index + 1];
    }

    public static <T extends Enum<T>> T previous(T value) {
        int index = value.ordinal();
        T[] values = (T[]) value.getClass().getEnumConstants();

        if (index == 0) return values[values.length - 1];
        return values[index - 1];
    }


}
