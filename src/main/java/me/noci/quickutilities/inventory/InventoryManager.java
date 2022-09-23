package me.noci.quickutilities.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryManager extends BukkitRunnable implements Listener {

    public InventoryManager(JavaPlugin plugin) {
        runTaskTimer(plugin, 20, 10);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
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

    @EventHandler
    public void handleInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof ProvidedInventoryHolder) || event.getClickedInventory() == null)
            return;

        Player player = (Player) event.getWhoClicked();
        ProvidedInventoryHolder inventoryHolder = (ProvidedInventoryHolder) event.getInventory().getHolder();

        event.setCancelled(inventoryHolder.getProvider().isCancelledClick());

        if (event.getClickedInventory().equals(event.getView().getBottomInventory())) return;
        inventoryHolder.getContent().getSlot(event.getSlot()).getClickHandler().handle(player);
    }

}
