package me.noci.quickutilities.quicktab.utils;

import java.util.function.BiFunction;

/**
 * This is a wrapper for {@link BiFunction}. The two types of the arguments will be mapped to only one type.
 *
 * @param <T> The type of the first and second argument of the function
 * @param <R> The type of the result of the function
 */
public interface TabListFunction<T, R> extends BiFunction<T, T, R> {

}
