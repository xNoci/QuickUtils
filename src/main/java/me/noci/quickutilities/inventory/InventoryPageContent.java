package me.noci.quickutilities.inventory;

import org.bukkit.inventory.ItemStack;

public interface InventoryPageContent {

    void itemSlots(int... slots);

    void content(int index, GuiItem item);

    void addContent(GuiItem... content);

    void pageContent(GuiItem... content);

    void clearContent();

    GuiItem content(int index);

    int currentPage();

    int pageCount();

    int pageSize();

    int contentSize();

    boolean isFirstPage();

    boolean isLastPage();

    void previousPage();

    void nextPage();

    void previousPageButton(int slot, ItemStack navigateItem, ItemStack defaultItem);

    void nextPageButton(int slot, ItemStack navigateItem, ItemStack defaultItem);

}
