package me.noci.quickutilities.inventory;

import lombok.AccessLevel;
import lombok.Setter;
import me.noci.quickutilities.utils.Require;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class PageContent {

    @Setter(AccessLevel.PROTECTED) private GuiHolder handle;
    private int currentPage = 0;
    private int[] itemSlots = new int[0];
    private GuiItem[] pageContent = new GuiItem[0];
    private NavigationItem previousPageItem = null;
    private NavigationItem nextPageItem = null;

    PageContent() {
        //SEAL FOR OUTSIDE
    }

    private int getItemsPerPage() {
        return itemSlots.length;
    }

    private int getPageCount() {
        int itemsPerPage = getItemsPerPage();
        int itemCount = pageContent.length;
        int pageCount = (int) Math.ceil((double) itemCount / (double) itemsPerPage);

        if (pageCount > 0) pageCount -= 1;
        return pageCount;
    }

    private GuiItem[] getPageItems() {
        return Arrays.copyOfRange(pageContent, currentPage * getItemsPerPage(), (currentPage + 1) * getItemsPerPage());
    }

    public void setPageContent(GuiItem... pageContent) {
        this.pageContent = pageContent;
    }

    public void setItem(int index, GuiItem item) {
        Require.nonNull(pageContent, "pageContent cannot be null");
        Require.checkState(index <= pageContent.length, "index (%s) has to be less or equal to pageContent.length (%s)", index, pageContent.length);
        pageContent[index] = item;
    }

    public GuiItem getItem(int index) {
        Require.nonNull(pageContent, "pageContent cannot be null");
        Require.checkState(index <= pageContent.length, "index (%s) has to be less or equal to pageContent.length (%s)", index, pageContent.length);
        return pageContent[index];
    }

    public int getTotalItemCount() {
        return this.pageContent.length;
    }

    public void setItemSlots(int... slots) {
        this.itemSlots = slots;
    }

    public boolean isFirstPage() {
        return currentPage == 0;
    }

    public boolean isLastPage() {
        return getPageCount() == currentPage;
    }

    public void previousPage() {
        if (isFirstPage()) return;
        currentPage--;
        updatePage();
    }

    public void nextPage() {
        if (isLastPage()) return;
        currentPage++;
        updatePage();
    }

    public void updatePage() {
        fillSlots();
        setPageItems();

        this.handle.applyContent();
    }

    private void fillSlots() {
        GuiItem[] items = getPageItems();
        for (int i = 0; i < itemSlots.length; i++) {
            int slot = itemSlots[i];

            if (i > items.length) {
                this.handle.getContent().setItem(slot, null);
                continue;
            }

            GuiItem item = items[i];
            this.handle.getContent().setItem(slot, item);
        }
    }

    private void setPageItems() {
        if (previousPageItem != null) {
            int slot = previousPageItem.slot;
            GuiItem item = GuiItem.empty();
            item.setItem(!isFirstPage() ? previousPageItem.displayItem : previousPageItem.defaultItem);
            item.setAction(event -> previousPage());
            this.handle.getContent().setItem(slot, item);
        }

        if (nextPageItem != null) {
            int slot = nextPageItem.slot;
            GuiItem item = GuiItem.empty();
            item.setItem(!isLastPage() ? nextPageItem.displayItem : nextPageItem.defaultItem);
            item.setAction(event -> nextPage());
            this.handle.getContent().setItem(slot, item);
        }
    }

    public void setNextPageItem(int slot, ItemStack displayItem, ItemStack defaultItem) {
        this.nextPageItem = new NavigationItem();
        this.nextPageItem.slot = slot;
        this.nextPageItem.displayItem = displayItem;
        this.nextPageItem.defaultItem = defaultItem;
    }

    public void setPreviousPageItem(int slot, ItemStack displayItem, ItemStack defaultItem) {
        this.previousPageItem = new NavigationItem();
        this.previousPageItem.slot = slot;
        this.previousPageItem.displayItem = displayItem;
        this.previousPageItem.defaultItem = defaultItem;
    }

    private static class NavigationItem {
        private int slot = -1;
        private ItemStack displayItem = null;
        private ItemStack defaultItem = null;
    }


}
