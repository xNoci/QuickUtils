package me.noci.quickutilities.qcommand.mappings;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface PlayerMapping<T> extends Mapping<T, Player> {
    @Override
    T map(Player sender);
}
