package me.noci.quickutilities.utils.formatter;

@FunctionalInterface
public interface FormatKeyConverter<T> {
    String convert(T obj);
}
