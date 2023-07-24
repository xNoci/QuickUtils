package me.noci.quickutilities.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    public static <T extends Enum<T>> T random(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        return values[RandomUtils.random().nextInt(values.length)];
    }

    public static <T extends Enum<T>> String join(String delimiter, Class<T> enumType) {
        return join(delimiter, enumType.getEnumConstants());
    }

    public static <T extends Enum<T>> String join(String delimiter, T[] values) {
        return join(delimiter, List.of(values));
    }

    public static <T extends Enum<T>> String join(String delimiter, List<T> values) {
        String[] names = values.stream().map(Enum::name).toArray(String[]::new);
        return String.join(delimiter, names);
    }

    public static <T extends Enum<T>> Optional<T> getIfPresent(Class<T> enumClass, String value) {
        return getIfPresent(enumClass, value, false);
    }

    public static <T extends Enum<T>> Optional<T> getIfPresent(Class<T> enumClass, String value, boolean ignoreCase) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> ignoreCase ? e.name().equalsIgnoreCase(value) : e.name().equals(value))
                .findFirst();
    }

}
