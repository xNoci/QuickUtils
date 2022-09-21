package me.noci.quickutilities.events.gui;

import lombok.Getter;
import me.noci.quickutilities.events.core.CorePlayerEvent;
import me.noci.quickutilities.quickgui.QuickGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

/**
 * This event will be called when a player clicks a not empty slot in a {@link QuickGUI} inventory.
 */
public class GuiClickEvent extends CorePlayerEvent {

    /**
     * The {@link QuickGUI} that was clicked.
     */
    @Getter private final QuickGUI gui;
    /**
     * The ItemStack which was clicked.
     */
    @Getter private final ItemStack clickedItem;
    /**
     * The {@link ClickType Type of click} that was used.
     */
    @Getter private final ClickType click;
    /**
     * The {@link InventoryAction} that was used.
     */
    @Getter private final InventoryAction action;
    /**
     * Whether this event was already handled by a set {@link me.noci.quickutilities.quickgui.ClickHandler ClickHandler} of the clicked {@link QuickGUI}.
     * <br>Only one specific {@link me.noci.quickutilities.quickgui.ClickHandler ClickHandler} can be called be for this event will trigger via {@link org.bukkit.plugin.PluginManager#callEvent(Event) PluginManager#callEvent(Event)}.
     */
    @Getter private final boolean alreadyHandled;

    public GuiClickEvent(Player player, QuickGUI gui, ItemStack clickedItem, ClickType click, InventoryAction action, boolean alreadyHandled) {
        super(player);
        this.gui = gui;
        this.clickedItem = clickedItem;
        this.click = click;
        this.action = action;
        this.alreadyHandled = alreadyHandled;
    }

}
