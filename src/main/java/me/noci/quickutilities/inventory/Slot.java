package me.noci.quickutilities.inventory;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Slot {

    @Getter private final SlotPos position;
    private GuiItem item = GuiItem.empty();

    public Slot(SlotPos position) {
        this.position = position;
    }

    public void setItem(GuiItem item) {
        this.item = item == null ? GuiItem.empty() : item;
    }

    public boolean isEmpty() {
        return this.item.getItemStack().getType() == Material.AIR;
    }

    public ItemStack getItemStack() {
        return this.item.getItemStack();
    }

    public ClickHandler getAction() {
        return this.item.getAction();
    }

    @Override
    public String toString() {
        return "Slot{position=%s, itemStack={type=%s, displayName=%s}, clickHandler=%s}".
                formatted(
                        position,
                        getItemStack().getType(),
                        getItemStack().getItemMeta() != null ? getItemStack().getItemMeta().getDisplayName() : "null",
                        getAction() != ClickHandler.DEFAULT
                );
    }
}
