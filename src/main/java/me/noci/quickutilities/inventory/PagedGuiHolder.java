package me.noci.quickutilities.inventory;

import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class PagedGuiHolder extends GuiHolder implements InventoryPageContent {

    private int currentPage = 1;
    private int[] itemSlots = new int[0];
    private List<GuiItem> pageContent = Lists.newArrayList();
    private NavigationItem previousPageItem = null;
    private NavigationItem nextPageItem = null;

    public PagedGuiHolder(PagedQuickGUIProvider provider, int size, Component title) {
        super(provider, size, title);
    }

    public PagedGuiHolder(PagedQuickGUIProvider provider, InventoryType type, Component title) {
        super(provider, type, title);
    }

    public PagedGuiHolder(PagedQuickGUIProvider provider, InventoryType type, int size, Component title) {
        super(provider, type, size, title);
    }

    @Override
    public void applyContent() {
        super.applyContent();
        updateContent();
    }

    protected void updateContent() {
        List<GuiItem> itemsOnPage = pageContent.stream().skip((long) (currentPage - 1) * pageSize()).limit(pageSize()).toList();
        Consumer<Integer> setItem = slot -> setItem(slot, slot < itemsOnPage.size() ? itemsOnPage.get(slot) : null);
        IntStream.iterate(0, slot -> slot < pageSize(), slot -> slot + 1).forEach(setItem::accept);

        if (previousPageItem != null) {
            int slot = previousPageItem.slot;
            GuiItem item = GuiItem.empty();
            item.setItem(!isFirstPage() ? previousPageItem.navigateItem : previousPageItem.defaultItem);
            item.setAction(event -> previousPage());
            setItem(slot, item);
        }

        if (nextPageItem != null) {
            int slot = nextPageItem.slot;
            GuiItem item = GuiItem.empty();
            item.setItem(!isLastPage() ? nextPageItem.navigateItem : nextPageItem.defaultItem);
            item.setAction(event -> nextPage());
            setItem(slot, item);
        }

    }


    @Override
    public void itemSlots(int... slots) {
        this.itemSlots = slots;
    }

    @Override
    public void content(int index, GuiItem item) {
        this.pageContent.set(index, item);
    }

    @Override
    public void addContent(GuiItem... content) {
        this.pageContent.addAll(Lists.newArrayList(content));
    }

    @Override
    public void pageContent(GuiItem... content) {
        this.pageContent = Lists.newArrayList(content);
    }

    @Override
    public void clearContent() {
        this.pageContent.clear();
    }

    @Override
    public GuiItem content(int index) {
        return this.pageContent.get(index);
    }

    @Override
    public int currentPage() {
        return this.currentPage;
    }

    @Override
    public int pageCount() {
        int contentSize = contentSize();
        int pageSize = pageSize();
        return contentSize / pageSize + (contentSize % pageSize == 0 ? 0 : 1);
    }

    @Override
    public int pageSize() {
        return this.itemSlots.length;
    }

    @Override
    public int contentSize() {
        return this.pageContent.size();
    }

    @Override
    public boolean isFirstPage() {
        return currentPage == 1;
    }

    @Override
    public boolean isLastPage() {
        return pageCount() == currentPage;
    }

    @Override
    public void previousPage() {
        if (isFirstPage()) return;
        currentPage--;
        updateContent();
    }

    @Override
    public void nextPage() {
        if (isLastPage()) return;
        currentPage++;
        updateContent();
    }

    @Override
    public void previousPageButton(int slot, ItemStack navigateItem, ItemStack defaultItem) {
        this.previousPageItem = new NavigationItem(slot, navigateItem, defaultItem);
    }

    @Override
    public void nextPageButton(int slot, ItemStack navigateItem, ItemStack defaultItem) {
        this.nextPageItem = new NavigationItem(slot, navigateItem, defaultItem);
    }

    private record NavigationItem(int slot, ItemStack navigateItem, ItemStack defaultItem) {
    }

}
