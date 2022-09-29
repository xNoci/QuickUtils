package me.noci.quickutilities.inventory;

import com.google.common.base.Preconditions;
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
        Preconditions.checkNotNull(item, "Gui Item cannot be null");
        this.item = item;
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
        return String.format("Slot{position=%s, itemStack={type=%s, displayName=%s}, clickHandler=%s}",
                position,
                getItemStack().getType(),
                getItemStack().getItemMeta() != null ? getItemStack().getItemMeta().getDisplayName() : "null",
                getAction() != ClickHandler.DEFAULT);

    }
}
