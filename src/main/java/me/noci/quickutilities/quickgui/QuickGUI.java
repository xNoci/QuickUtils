package me.noci.quickutilities.quickgui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class QuickGUI implements InventoryHolder {

    private final Inventory handle;

    /**
     * Creates a {@link QuickGUI} with a custom size.
     *
     * @param size The size of the inventory
     */
    public QuickGUI(int size) {
        this(InventoryType.CHEST.getDefaultTitle(), size);
    }

    /**
     * Creates a {@link QuickGUI} with a custom title and size.
     *
     * @param title The title of the inventory
     * @param size  The size of the inventory
     */
    public QuickGUI(String title, int size) {
        this(InventoryType.CHEST, title, size);
    }

    /**
     * Creates a {@link QuickGUI} with a custom type.
     *
     * @param type The type of the inventory
     */
    public QuickGUI(InventoryType type) {
        this(type, type.getDefaultTitle());
    }

    /**
     * Creates a {@link QuickGUI} with a custom type and title.
     *
     * @param type  The type of the inventory
     * @param title The title of the inventory
     */
    public QuickGUI(InventoryType type, String title) {
        this(type, title, 0);
    }

    private QuickGUI(InventoryType type, String title, int size) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(title, "title");

        if (type == InventoryType.CHEST && size > 0) {
            this.handle = Bukkit.createInventory(this, size, title);
        } else {
            this.handle = Bukkit.createInventory(this, type, title);
        }
    }

    @Override
    public Inventory getInventory() {
        return this.handle;
    }

    public String getTitle() {
        return this.handle.getTitle();
    }

    public int getSize() {
        return this.handle.getSize();
    }

    public void openInventory(Player player) {
        player.openInventory(this.handle);
    }

    /**
     * Add the given ItemStack to the next free slot.
     *
     * @param itemStack The ItemStack which will be added.
     */
    public void addItem(ItemStack... itemStack) {
        this.handle.addItem(itemStack);
    }

    /**
     * Set the given ItemStack to the given slots.
     *
     * @param itemStack The ItemStack which will be set
     * @param slots     The slot in which the ItemStack will be set.
     */
    public void setItem(ItemStack itemStack, int... slots) {
        for (int slot : slots) {
            this.handle.setItem(slot, itemStack);
        }
    }

    /**
     * Removes the items from the given slots.
     *
     * @param slots The slots which will be cleared
     */
    public void removeItem(int... slots) {
        for (int slot : slots) {
            this.handle.clear(slot);
        }
    }

}
