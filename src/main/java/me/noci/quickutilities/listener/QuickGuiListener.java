package me.noci.quickutilities.listener;

import me.noci.quickutilities.events.gui.GuiClickEvent;
import me.noci.quickutilities.events.gui.GuiCloseEvent;
import me.noci.quickutilities.events.gui.GuiOpenEvent;
import me.noci.quickutilities.quickgui.ClickHandler;
import me.noci.quickutilities.quickgui.QuickGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class QuickGuiListener implements Listener {

    @EventHandler
    public void handleInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof QuickGUI) || event.getClickedInventory() == null) return;
        event.setCancelled(true);

        if (event.getClickedInventory().equals(event.getView().getBottomInventory())) return;
        Player player = (Player) event.getWhoClicked();
        QuickGUI quickGUI = (QuickGUI) event.getInventory().getHolder();
        ItemStack item = event.getCurrentItem();
        ClickType clickType = event.getClick();
        InventoryAction action = event.getAction();
        if (item == null || item.getType() == Material.AIR) return;

        Optional<ClickHandler> clickHandler = quickGUI.getClickHandler(event.getSlot());
        clickHandler.ifPresent(handler -> handler.handle(new GuiClickEvent(player, quickGUI, item, clickType, action, false)));

        GuiClickEvent clickEvent = new GuiClickEvent(player, quickGUI, item, clickType, action, clickHandler.isPresent());
        Bukkit.getPluginManager().callEvent(clickEvent);
    }

}
