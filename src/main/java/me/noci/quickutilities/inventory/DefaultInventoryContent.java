package me.noci.quickutilities.inventory;

import com.google.common.base.Preconditions;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class DefaultInventoryContent implements InventoryContent {

    private final Slot[] slots;
    private final int size;

    protected DefaultInventoryContent(InventoryType type, int size) {
        this.size = size;
        this.slots = new Slot[size];

        for (int i = 0; i < size; i++) {
            SlotPos slotPos = new SlotPos(type, i);
            slots[i] = new Slot(slotPos);
        }
    }

    @Override
    public Slot getSlot(int slot) {
        Preconditions.checkElementIndex(slot, slots.length, "Slot number");
        return slots[slot];
    }

    @Override
    public void setSlot(int slot, ItemStack itemStack, ClickHandler clickHandler) {
        Preconditions.checkElementIndex(slot, slots.length, "Slot number");
        Slot s = slots[slot];
        s.setItemStack(itemStack);
        s.setClickHandler(clickHandler);
    }

    @Override
    public void addItem(ItemStack itemStack, ClickHandler clickHandler) {
        for (Slot slot : slots) {
            if (!slot.isEmpty()) continue;
            slot.setItemStack(itemStack);
            slot.setClickHandler(clickHandler);
            break;
        }
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        Preconditions.checkElementIndex(slot, slots.length, "Slot number");
        slots[slot].setItemStack(itemStack);
    }

    @Override
    public void setClickHandler(int slot, ClickHandler clickHandler) {
        Preconditions.checkElementIndex(slot, slots.length, "Slot number");
        slots[slot].setClickHandler(clickHandler);
    }

    @Override
    public void fillPattern(ItemStack itemStack, InventoryPattern... patterns) {
        for (InventoryPattern pattern : patterns) {
            for (int slot : pattern.getSlots(this.size)) {
                setItem(slot, itemStack);
            }
        }
    }

}
