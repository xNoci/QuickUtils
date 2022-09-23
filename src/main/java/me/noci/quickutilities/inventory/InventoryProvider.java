package me.noci.quickutilities.inventory;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public abstract class InventoryProvider {

    private final InventoryType type;
    private final String title;
    private final int size;

    protected InventoryProvider(int size) {
        this(InventoryType.CHEST.getDefaultTitle(), size);
    }

    protected InventoryProvider(String title, int size) {
        this(InventoryType.CHEST, title, size);
    }

    protected InventoryProvider(InventoryType type) {
        this(type, type.getDefaultTitle());
    }

    protected InventoryProvider(InventoryType type, String title) {
        this(type, title, type.getDefaultSize());
    }

    private InventoryProvider(InventoryType type, String title, int size) {
        Preconditions.checkNotNull(type, "InventoryType cannot be null");
        Preconditions.checkNotNull(title, "Title cannot be null");

        this.type = type;
        this.title = title;
        this.size = size;
    }

    public abstract void init(Player player, InventoryContent content);

    public void update(Player player, InventoryContent content) {
    }

    public void provide(Player player) {
        ProvidedInventoryHolder inventoryHolder = new ProvidedInventoryHolder(this, new InventoryContent(this.type, this.size > 0 ? this.size : this.type.getDefaultSize()));
        Inventory inventory;

        if (this.type == InventoryType.CHEST && this.size > 0) {
            inventory = Bukkit.createInventory(inventoryHolder, this.size, this.title);
        } else {
            inventory = Bukkit.createInventory(inventoryHolder, this.type, this.title);
        }
        inventoryHolder.setInventory(inventory);
        init(player, inventoryHolder.getContent());

        inventoryHolder.applyContent();

        player.openInventory(inventory);
    }

}
