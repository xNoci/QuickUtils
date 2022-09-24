package me.noci.quickutilities.inventory;

import lombok.AccessLevel;
import lombok.Getter;
import me.noci.quickutilities.utils.QuickItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Slot {

    private static final ItemStack DEFAULT_ITEM_STACK = new QuickItemStack(Material.AIR);

    @Getter private final SlotPos position;
    @Getter private ItemStack itemStack = DEFAULT_ITEM_STACK;
    @Getter(AccessLevel.PROTECTED) private ClickHandler clickHandler = ClickHandler.DEFAULT;

    public Slot(SlotPos position) {
        this.position = position;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack == null ? DEFAULT_ITEM_STACK : itemStack;
    }


    public void setClickHandler(ClickHandler clickHandler) {
        this.clickHandler = clickHandler == null ? ClickHandler.DEFAULT : clickHandler;
    }

    public boolean isEmpty() {
        return this.itemStack.getType() == Material.AIR;
    }

    @Override
    public String toString() {
        return String.format("Slot{position=%s, itemStack={type=%s, displayName=%s}, clickHandler=%s}",
                position,
                itemStack.getType(),
                itemStack.getItemMeta() != null ? itemStack.getItemMeta().getDisplayName() : "null",
                clickHandler != ClickHandler.DEFAULT);

    }
}
