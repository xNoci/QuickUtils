package me.noci.quickutilities.inventory;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface GuiProvider {

    void provide(Player player);

}
