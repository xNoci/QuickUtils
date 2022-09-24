package me.noci.quickutilities.inventory;

import lombok.Getter;
import me.noci.quickutilities.events.core.CorePlayerCancellableEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

public class SlotClickEvent extends CorePlayerCancellableEvent {

    @Getter private final Slot slot;
    @Getter private final ItemStack clickedItem;
    @Getter private final ClickType click;
    @Getter private final InventoryAction action;

    public SlotClickEvent(Player player, Slot slot, ItemStack clickedItem, ClickType click, InventoryAction action) {
        super(player, true);
        this.slot = slot;
        this.clickedItem = clickedItem;
        this.click = click;
        this.action = action;
    }

}
