package me.noci.quickutilities.quicktab.utils;

import org.bukkit.entity.Player;

import java.util.function.BiFunction;

/**
 * This is a wrapper for {@link BiFunction}. The two types of the arguments will be mapped to only one type.
 *
 * @param <R> The type of the result of the function
 */
public interface TabListFunction<R> extends BiFunction<Player, Player, R> {

}
