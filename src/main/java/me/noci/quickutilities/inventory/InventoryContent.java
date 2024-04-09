package me.noci.quickutilities.inventory;

import me.noci.quickutilities.utils.InventoryPattern;

public interface InventoryContent {

    void applyContent();

    Slot getSlot(int slot);

    void setItem(int slot, GuiItem item);

    void addItem(GuiItem item);

    void clearItem(int slot);

    /**
     * Fill the complete inventory with the given ItemStack.
     *
     * @param item The ItemStack that will be used
     */
    default void fill(GuiItem item) {
        fillPattern(item, InventoryPattern.FULL);
    }

    /**
     * Fill the border of the inventory with the given ItemStack.
     *
     * @param item The ItemStack that will be used
     */
    default void fillBorders(GuiItem item) {
        fillPattern(item, InventoryPattern.TOP, InventoryPattern.BOTTOM, InventoryPattern.LEFT, InventoryPattern.RIGHT);
    }

    /**
     * Fill the corner of the inventory with the given ItemStack.
     *
     * @param item The ItemStack that will be used
     */
    default void fillCorners(GuiItem item) {
        fillPattern(item, InventoryPattern.TOP_LEFT_CORNER, InventoryPattern.TOP_RIGHT_CORNER, InventoryPattern.BOTTOM_LEFT_CORNER, InventoryPattern.BOTTOM_RIGHT_CORNER);
    }

    /**
     * Fill the inventory with the given patterns.
     *
     * @param item     The ItemStack that will be used
     * @param patterns The patterns which will be used
     */
    void fillPattern(GuiItem item, InventoryPattern... patterns);

    /**
     * Fill the inventory at the given slots with the given item.
     *
     * @param item  The ItemStack that will be used
     * @param slots The Slots that will be filled
     */
    void fillSlots(GuiItem item, int[] slots);

}
