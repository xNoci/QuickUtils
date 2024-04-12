package me.noci.quickutilities.inventory;

import com.google.common.base.Preconditions;
import lombok.Getter;
import me.noci.quickutilities.utils.Legacy;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

@Getter
public abstract class QuickGUIProvider implements GuiProvider {

    final InventoryType type;
    final String title;
    final int size;

    protected QuickGUIProvider(int size) {
        this(InventoryType.CHEST.getDefaultTitle(), size);
    }

    protected QuickGUIProvider(String title, int size) {
        this(InventoryType.CHEST, title, size);
    }

    protected QuickGUIProvider(Component title, int size) {
        this(InventoryType.CHEST, Legacy.serialize(title), size);
    }

    protected QuickGUIProvider(InventoryType type) {
        this(type, type.getDefaultTitle());
    }

    protected QuickGUIProvider(InventoryType type, String title) {
        this(type, title, type.getDefaultSize());
    }

    protected QuickGUIProvider(InventoryType type, Component title) {
        this(type, Legacy.serialize(title), type.getDefaultSize());
    }

    QuickGUIProvider(InventoryType type, String title, int size) {
        Preconditions.checkNotNull(type, "InventoryType cannot be null");
        Preconditions.checkNotNull(title, "Title cannot be null");

        if (type == InventoryType.CHEST) {
            Preconditions.checkArgument(size % 9 == 0 && size >= 9 && size <= 54, "Size for custom inventory must be a multiple of 9 between 9 and 54 slots (got %s)".formatted(size));
        }

        this.type = type;
        this.title = title;
        this.size = size;
    }

    @Override
    public void provide(Player player) {
        DefaultInventoryContent inventoryContent = new DefaultInventoryContent(this.type, this.size > 0 ? this.size : this.type.getDefaultSize());
        GuiHolder inventoryHolder = new GuiHolder(this, inventoryContent);
        inventoryContent.setGuiHolder(inventoryHolder);
        Inventory inventory;

        if (this.type == InventoryType.CHEST && this.size > 0) {
            inventory = Bukkit.createInventory(inventoryHolder, this.size, this.title);
        } else {
            //Change dropper inventory to dispenser due to wierd IndexOutOfBoundException thrown by minecraft
            inventory = Bukkit.createInventory(inventoryHolder, this.type == InventoryType.DROPPER ? InventoryType.DISPENSER : this.type, this.title);
        }
        inventoryHolder.setInventory(inventory);
        init(player, inventoryHolder.getContent());

        inventoryHolder.applyContent();

        player.openInventory(inventory);
    }

    public boolean isCancelledClick() {
        return true;
    }

    public void update(Player player, InventoryContent content) {
    }

    public abstract void init(Player player, InventoryContent content);


}
