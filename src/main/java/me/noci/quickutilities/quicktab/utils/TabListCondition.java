package me.noci.quickutilities.quicktab.utils;

import java.util.function.BiPredicate;

/**
 * This is a wrapper for {@link BiPredicate}. The two types of the arguments will be mapped to only one type.
 *
 * @param <T> The type of the first and second argument to the predicate
 */
public interface TabListCondition<T> extends BiPredicate<T, T> {
}
