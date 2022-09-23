package me.noci.quickutilities.inventory;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface ClickHandler {

    ClickHandler DEFAULT = player -> {};

    void handle(Player player);

}
