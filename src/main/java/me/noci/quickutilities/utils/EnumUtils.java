package me.noci.quickutilities.utils;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class EnumUtils {

    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> T next(T value) {
        int index = value.ordinal();
        T[] values = (T[]) value.getDeclaringClass().getEnumConstants();

        if (index == values.length - 1) return values[0];
        return values[index + 1];
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> T previous(T value) {
        int index = value.ordinal();
        T[] values = (T[]) value.getDeclaringClass().getEnumConstants();

        if (index == 0) return values[values.length - 1];
        return values[index - 1];
    }

    public static <T extends Enum<?>> T random(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        return values[RandomUtils.random().nextInt(values.length)];
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

    public static <T extends Enum<?>> Optional<T> getIfPresent(Class<T> enumClass, String value) {
        return getIfPresent(enumClass, value, false);
    }

    public static <T extends Enum<?>> Optional<T> getIfPresent(Class<T> enumClass, String value, boolean ignoreCase) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> ignoreCase ? e.name().equalsIgnoreCase(value) : e.name().equals(value))
                .findFirst();
    }

    public static <T extends Enum<?>> T[] values(Class<T> enumClass) {
        return enumClass.getEnumConstants();
    }

    public static <T extends Enum<?>> List<T> asList(Class<T> enumClass) {
        return asList(enumClass.getEnumConstants());
    }

    public static <T extends Enum<?>> List<T> asList(T[] values) {
        return Lists.newArrayList(values);
    }

    public static <T extends Enum<?>> Stream<T> asStream(Class<T> enumClass) {
        return asStream(enumClass.getEnumConstants());
    }

    public static <T extends Enum<?>> Stream<T> asStream(T[] values) {
        return asList(values).stream();
    }

    public static <T extends Enum<?>> T at(Class<T> enumClass, int index) {
        T[] values = enumClass.getEnumConstants();
        return values[index % values.length];
    }

}
