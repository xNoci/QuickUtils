package me.noci.quickutilities.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public abstract class PagedQuickGUIProvider extends QuickGUIProvider {

    protected PagedQuickGUIProvider(int size) {
        this(InventoryType.CHEST.getDefaultTitle(), size);
    }

    protected PagedQuickGUIProvider(String title, int size) {
        this(InventoryType.CHEST, title, size);
    }

    protected PagedQuickGUIProvider(InventoryType type) {
        this(type, type.getDefaultTitle());
    }

    protected PagedQuickGUIProvider(InventoryType type, String title) {
        this(type, title, type.getDefaultSize());
    }

    PagedQuickGUIProvider(InventoryType type, String title, int size) {
        super(type, title, size);
    }


    @Override
    public void provide(Player player) {
        GuiHolder inventoryHolder = new GuiHolder(this, new DefaultInventoryContent(this.type, this.size > 0 ? this.size : this.type.getDefaultSize()));
        Inventory inventory;

        if (this.type == InventoryType.CHEST && this.size > 0) {
            inventory = Bukkit.createInventory(inventoryHolder, this.size, this.title);
        } else {
            //Change dropper inventory to dispenser due to wierd IndexOutOfBoundException thrown by minecraft
            inventory = Bukkit.createInventory(inventoryHolder, this.type == InventoryType.DROPPER ? InventoryType.DISPENSER : this.type, this.title);
        }
        inventoryHolder.setInventory(inventory);
        init(player, inventoryHolder.getContent());

        PageContent pageContent = new PageContent();
        inventoryHolder.setPageContent(pageContent);
        initPage(player, pageContent);

        inventoryHolder.applyContent();
        player.openInventory(inventory);
    }

    public abstract void initPage(Player player, PageContent pageContent);

}
