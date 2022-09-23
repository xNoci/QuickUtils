package me.noci.quickutilities.inventory;

import com.google.common.base.Preconditions;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class InventoryContent {

    private final Slot[] slots;
    private final int size;

    public InventoryContent(InventoryType type, int size) {
        this.size = size;
        this.slots = new Slot[size];

        for (int i = 0; i < size; i++) {
            SlotPos slotPos = new SlotPos(type, i);
            slots[i] = new Slot(slotPos);
        }
    }

    public void addItem(ItemStack itemStack) {
        addItem(itemStack, null);
    }

    public void addItem(ItemStack itemStack, ClickHandler clickHandler) {
        for (Slot slot : slots) {
            if (!slot.isEmpty()) continue;
            slot.setItemStack(itemStack);
            slot.setClickHandler(clickHandler);
            break;
        }
    }

    public void setItem(int slot, ItemStack itemStack) {
        Preconditions.checkElementIndex(slot, slots.length, "Slot number");
        slots[slot].setItemStack(itemStack);
    }

    public void setClickHandler(int slot, ClickHandler clickHandler) {
        Preconditions.checkElementIndex(slot, slots.length, "Slot number");
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
        Preconditions.checkElementIndex(slot, slots.length, "Slot number");
        return slots[slot];
    }

    public void setSlot(int slot, ItemStack itemStack, ClickHandler clickHandler) {
        Preconditions.checkElementIndex(slot, slots.length, "Slot number");
        Slot s = slots[slot];
        s.setItemStack(itemStack);
        s.setClickHandler(clickHandler);
    }

    /**
     * Fill the complete inventory with the given ItemStack.
     *
     * @param itemStack The ItemStack that will be used
     */
    public void fill(ItemStack itemStack) {
        fillPattern(itemStack, InventoryPattern.FULL);
    }

    /**
     * Fill the border of the inventory with the given ItemStack.
     *
     * @param itemStack The ItemStack that will be used
     */
    public void fillBorders(ItemStack itemStack) {
        fillPattern(itemStack, InventoryPattern.TOP, InventoryPattern.BOTTOM, InventoryPattern.LEFT, InventoryPattern.RIGHT);
    }

    /**
     * Fill the corner of the inventory with the given ItemStack.
     *
     * @param itemStack The ItemStack that will be used
     */
    public void fillCorners(ItemStack itemStack) {
        fillPattern(itemStack, InventoryPattern.TOP_LEFT_CORNER, InventoryPattern.TOP_RIGHT_CORNER, InventoryPattern.BOTTOM_LEFT_CORNER, InventoryPattern.BOTTOM_RIGHT_CORNER);
    }

    /**
     * Fill the inventory with the given patterns.
     *
     * @param itemStack The ItemStack that will be used
     * @param patterns  The patterns which will be used
     */
    public void fillPattern(ItemStack itemStack, InventoryPattern... patterns) {
        for (InventoryPattern pattern : patterns) {
            for (int slot : pattern.getSlots(this.size)) {
                setItem(slot, itemStack);
            }
        }
    }

}
