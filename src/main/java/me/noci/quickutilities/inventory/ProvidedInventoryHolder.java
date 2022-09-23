package me.noci.quickutilities.inventory;

import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ProvidedInventoryHolder implements InventoryHolder {

    @Getter private final InventoryProvider provider;
    @Getter private final InventoryContent content;
    private Inventory handle;

    public ProvidedInventoryHolder(InventoryProvider provider, InventoryContent content) {
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

    public void applyContent() {
        this.handle.clear();
        for (int i = 0; i < this.handle.getSize(); i++) {
            this.handle.setItem(i, content.getSlot(i).getItemStack());
        }
    }
}
