package me.noci.quickutilities.inventory;

import me.noci.quickutilities.utils.InventoryPattern;
import org.bukkit.inventory.ItemStack;

public interface InventoryContent {

    Slot getSlot(int slot);

    void setSlot(int slot, ItemStack itemStack, ClickHandler clickHandler);

    default void addItem(ItemStack itemStack) {
        addItem(itemStack, null);
    }

    void addItem(ItemStack itemStack, ClickHandler clickHandler);

    void setItem(int slot, ItemStack itemStack);

    default void removeItem(int slot) {
        setItem(slot, null);
    }

    void setClickHandler(int slot, ClickHandler clickHandler);

    default void removeClickHandler(int slot) {
        setClickHandler(slot, null);
    }

    default void clear(int slot) {
        removeClickHandler(slot);
        removeItem(slot);
    }

    /**
     * Fill the complete inventory with the given ItemStack.
     *
     * @param itemStack The ItemStack that will be used
     */
    default void fill(ItemStack itemStack) {
        fillPattern(itemStack, InventoryPattern.FULL);
    }

    /**
     * Fill the border of the inventory with the given ItemStack.
     *
     * @param itemStack The ItemStack that will be used
     */
    default void fillBorders(ItemStack itemStack) {
        fillPattern(itemStack, InventoryPattern.TOP, InventoryPattern.BOTTOM, InventoryPattern.LEFT, InventoryPattern.RIGHT);
    }

    /**
     * Fill the corner of the inventory with the given ItemStack.
     *
     * @param itemStack The ItemStack that will be used
     */
    default void fillCorners(ItemStack itemStack) {
        fillPattern(itemStack, InventoryPattern.TOP_LEFT_CORNER, InventoryPattern.TOP_RIGHT_CORNER, InventoryPattern.BOTTOM_LEFT_CORNER, InventoryPattern.BOTTOM_RIGHT_CORNER);
    }

    /**
     * Fill the inventory with the given patterns.
     *
     * @param itemStack The ItemStack that will be used
     * @param patterns  The patterns which will be used
     */
    void fillPattern(ItemStack itemStack, InventoryPattern... patterns);

}
