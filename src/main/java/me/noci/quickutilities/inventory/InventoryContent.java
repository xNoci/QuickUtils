package me.noci.quickutilities.inventory;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class InventoryContent {

    private final Slot[] slots;

    public InventoryContent(InventoryType type, int size) {
        this.slots = new Slot[size];

        for (int i = 0; i < size; i++) {
            SlotPos slotPos = new SlotPos(type, i);
            slots[i] = new Slot(slotPos);
        }
    }

    public void setItem(int slot, ItemStack itemStack) {
        if (slot > slots.length) return; //TODO Throw exception
        slots[slot].setItemStack(itemStack);
    }

    public void setClickHandler(int slot, ClickHandler clickHandler) {
        if (slot > slots.length) return; //TODO Throw exception
        slots[slot].setClickHandler(clickHandler);
    }

    public void removeItemStack(int slot) {
        setItem(slot, null);
    }

    public void removeClickHandler(int slot) {
        setClickHandler(slot, null);
    }

    public void clear(int slot) {
        removeClickHandler(slot);
        removeItemStack(slot);
    }

    public Slot getSlot(int slot) {
        if (slot > slots.length) return null; //TODO Throw exception
        return slots[slot];
    }


}
