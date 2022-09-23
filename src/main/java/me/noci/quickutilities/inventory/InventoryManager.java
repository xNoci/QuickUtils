package me.noci.quickutilities.inventory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryManager extends BukkitRunnable {

    public InventoryManager(JavaPlugin plugin) {
        runTaskTimer(plugin, 20, 10);
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getOpenInventory().getTopInventory() != null)
                .filter(player -> player.getOpenInventory().getTopInventory().getHolder() instanceof ProvidedInventoryHolder)
                .forEach(player -> {
                    Inventory inventory = player.getOpenInventory().getTopInventory();
                    ProvidedInventoryHolder inventoryHolder = (ProvidedInventoryHolder) inventory.getHolder();

                    inventoryHolder.getProvider().update(player, inventoryHolder.getContent());
                    inventoryHolder.applyContent();
                });
    }

}
