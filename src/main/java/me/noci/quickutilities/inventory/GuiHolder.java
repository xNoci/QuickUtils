package me.noci.quickutilities.inventory;

import lombok.Getter;
import me.noci.quickutilities.utils.Require;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class GuiHolder implements InventoryHolder {

    static {
        GuiManager.initialise();
    }

    @Getter private final QuickGUIProvider provider;
    @Getter private final InventoryContent content;
    @Getter private PageContent pageContent = null;
    private final Inventory handle;

    public GuiHolder(QuickGUIProvider provider, int size, Component title) {
        this(provider, InventoryType.CHEST, size, title);
    }

    public GuiHolder(QuickGUIProvider provider, InventoryType type, Component title) {
        this(provider, type, type.getDefaultSize(), title);
    }

    public GuiHolder(QuickGUIProvider provider, InventoryType type, int size, Component title) {
        Require.nonNull(provider, "QuickGUIProvider cannot be null");

        this.provider = provider;
        this.content = new DefaultInventoryContent(this, type, size);

        if (type == InventoryType.CHEST && size > 0) {
            handle = Bukkit.createInventory(this, size, title);
        } else {
            //Change dropper inventory to dispenser due to wierd IndexOutOfBoundException thrown by minecraft
            handle = Bukkit.createInventory(this, type == InventoryType.DROPPER ? InventoryType.DISPENSER : type, title);
        }
    }

    public GuiHolder(@NotNull QuickGUIProvider provider, Inventory inventory) {
        Require.nonNull(provider, "QuickGUIProvider cannot be  null");
        Require.nonNull(inventory, "Inventory cannot be null");
        this.provider = provider;
        this.content = new DefaultInventoryContent(this, inventory.getType(), inventory.getSize());
        this.handle = inventory;
    }

    @Override
    @NotNull
    public Inventory getInventory() {
        return this.handle;
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
