package me.noci.quickutilities.utils;

import java.util.List;

public class EnumUtils {

    public static <T extends Enum<?>> T next(T value) {
        int index = value.ordinal();
        T[] values = (T[]) value.getClass().getEnumConstants();

        if (index == values.length - 1) return values[0];
        return values[index + 1];
    }

    public static <T extends Enum<?>> T previous(T value) {
        int index = value.ordinal();
        T[] values = (T[]) value.getClass().getEnumConstants();

        if (index == 0) return values[values.length - 1];
        return values[index - 1];
    }

    public static <T extends Enum<?>> String join(String delimiter, Class<T> enumType) {
        return join(delimiter, enumType.getEnumConstants());
    }

    public static <T extends Enum<?>> String join(String delimiter, T[] values) {
        return join(delimiter, List.of(values));
    }

    public static <T extends Enum<?>> String join(String delimiter, List<T> values) {
        String[] names = values.stream().map(Enum::name).toArray(String[]::new);
        return String.join(delimiter, names);
    }

}
