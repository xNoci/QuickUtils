package me.noci.quickutilities.inventory;

import me.noci.quickutilities.QuickUtils;
import me.noci.quickutilities.events.Events;
import me.noci.quickutilities.utils.BukkitUnit;
import me.noci.quickutilities.utils.Require;
import me.noci.quickutilities.utils.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public final class GuiManager {

    private GuiManager() {
        //SEAL CLASS
        //TODO Add animation system
    }

    static void initialise() {
        QuickUtils.instance().getLogger().info("Initialising gui manager");

        Events.subscribe(InventoryClickEvent.class)
                .filter(event -> event.getWhoClicked() instanceof Player)
                .filter(event -> event.getClickedInventory() != null)
                .filter(event -> !event.getView().getBottomInventory().equals(event.getClickedInventory()))
                .filter(event -> event.getInventory().getHolder() instanceof GuiHolder)
                .handle(event -> {
                    GuiHolder holder = (GuiHolder) event.getInventory().getHolder();
                    QuickGUIProvider provider = Require.nonNull(holder).getProvider();
                    event.setCancelled(provider.isCancelledClick());

                    Slot slot = holder.getSlot(event.getSlot());
                    SlotClickEvent clickEvent = new SlotClickEvent((Player) event.getWhoClicked(), slot, slot.getItemStack(), event.getClick(), event.getAction());
                    slot.fireClickEvent(clickEvent);
                });

        Scheduler.repeat(5, BukkitUnit.TICKS, () -> Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getOpenInventory().getTopInventory().getHolder() instanceof GuiHolder)
                .forEach(player -> {
                    Inventory inventory = player.getOpenInventory().getTopInventory();
                    GuiHolder inventoryHolder = (GuiHolder) inventory.getHolder();

                    inventoryHolder.getProvider().update(player, inventoryHolder);

                    if (inventoryHolder.hasPageContent()) {
                        PageContent pageContent = inventoryHolder.getPageContent();

                        if (inventoryHolder.getProvider() instanceof PagedQuickGUIProvider pagedProvider) {
                            pagedProvider.updatePageContent(player, pageContent);
                        }

                        pageContent.updatePage();
                    }

                    inventoryHolder.applyContent();
                }));
    }


}
