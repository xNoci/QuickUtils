package me.noci.quickutilities.input.functions;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface CanceledInput {
    void execute(Player player);
}
