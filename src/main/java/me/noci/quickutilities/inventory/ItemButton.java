package me.noci.quickutilities.inventory;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

public class ItemButton {

    public static ItemButton of(ItemStack item, ClickHandler action) {
        return new ItemButton(item, action);
    }

    public static ItemButton ofItem(ItemStack item) {
        return new ItemButton(item);
    }

    public static ItemButton empty() {
        return new ItemButton();
    }

    @Getter private ItemStack itemStack;
    @Getter private ClickHandler action;

    public ItemButton() {
        this(null, null);
    }

    public ItemButton(ItemStack itemStack) {
        this(itemStack, null);
    }

    private ItemButton(ItemStack itemStack, ClickHandler action) {
        this.itemStack = itemStack;
        this.action = action;
    }

    public void setItem(ItemStack item) {
        this.itemStack = item;
    }

    public void setAction(ClickHandler clickHandler) {
        this.action = clickHandler;
    }

}
