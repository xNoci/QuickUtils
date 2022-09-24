package me.noci.quickutilities.inventory;

import me.noci.quickutilities.utils.BukkitUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryManager extends BukkitRunnable implements Listener {

    public InventoryManager(JavaPlugin plugin) {
        runTaskTimer(plugin, 0, BukkitUnit.SECONDS.toTicks(1) / 2);
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
        InventoryProvider provider = inventoryHolder.getProvider();
        InventoryContent content = inventoryHolder.getContent();

        event.setCancelled(provider.isCancelledClick());
        if (event.getClickedInventory().equals(event.getView().getBottomInventory())) return;

        Slot slot = content.getSlot(event.getSlot());
        if (slot == null) return;

        ClickType clickType = event.getClick();
        InventoryAction action = event.getAction();
        SlotClickEvent clickEvent = new SlotClickEvent(player, slot, slot.getItemStack(), clickType, action);
        inventoryHolder.getContent().getSlot(event.getSlot()).getClickHandler().handle(clickEvent);
    }

}
