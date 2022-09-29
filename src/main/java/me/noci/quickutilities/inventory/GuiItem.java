package me.noci.quickutilities.inventory;

import lombok.Getter;
import me.noci.quickutilities.utils.QuickItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GuiItem {

    private static final ItemStack DEFAULT_ITEM_STACK = new QuickItemStack(Material.AIR);
    @Getter private ItemStack itemStack;
    @Getter private ClickHandler action;

    public GuiItem() {
        this(null, null);
    }

    public GuiItem(ItemStack itemStack) {
        this(itemStack, null);
    }

    private GuiItem(ItemStack itemStack, ClickHandler action) {
        setItem(itemStack);
        setAction(action);
    }

    public static GuiItem of(ItemStack item, ClickHandler action) {
        return new GuiItem(item, action);
    }

    public static GuiItem of(ItemStack item) {
        return new GuiItem(item);
    }

    public static GuiItem empty() {
        return new GuiItem();
    }

    public boolean isEmpty() {
        return this.itemStack == DEFAULT_ITEM_STACK || this.itemStack.getType() == Material.AIR;
    }

    public void setItem(ItemStack item) {
        this.itemStack = item == null ? DEFAULT_ITEM_STACK : item;
    }

    public void setAction(ClickHandler action) {
        this.action = action == null ? ClickHandler.DEFAULT : action;
    }

}
