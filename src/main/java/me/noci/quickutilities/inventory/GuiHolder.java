package me.noci.quickutilities.inventory;

import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GuiHolder implements InventoryHolder {

    static {
        GuiManager.initialise();
    }

    @Getter private final QuickGUIProvider provider;
    @Getter private final InventoryContent content;
    @Getter private PageContent pageContent = null;
    private Inventory handle;

    public GuiHolder(QuickGUIProvider provider, InventoryContent content) {
        this.provider = provider;
        this.content = content;
    }

    @Override
    public Inventory getInventory() {
        return this.handle;
    }

    protected void setInventory(Inventory inventory) {
        if (this.handle != null) return;
        this.handle = inventory;
    }

    protected void setPageContent(PageContent pageContent) {
        if (this.pageContent != null) return;
        this.pageContent = pageContent;
        this.pageContent.setHandle(this);
    }

    public void applyContent() {
        this.handle.clear();
        for (int i = 0; i < this.handle.getSize(); i++) {
            this.handle.setItem(i, content.getSlot(i).getItemStack());
        }
    }

    public boolean hasPageContent() {
        return pageContent != null;
    }
}
