package me.noci.quickutilities.inventory;

import com.google.common.base.Preconditions;
import me.noci.quickutilities.utils.InventoryPattern;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class DefaultInventoryContent implements InventoryContent {

    private final InventoryType type;
    private final int size;
    private final Slot[] content;

    protected DefaultInventoryContent(InventoryType type, int size) {
        this.type = type;
        this.size = size;
        this.content = new Slot[size];

        for (int i = 0; i < size; i++) {
            SlotPos slotPos = new SlotPos(type, i);
            this.content[i] = new Slot(slotPos);
        }
    }

    @Override
    public Slot getSlot(int slot) {
        if (slot > this.content.length) return null;
        return this.content[slot];
    }

    @Override
    public void setSlot(int slot, ItemStack itemStack, ClickHandler clickHandler) {
        Preconditions.checkElementIndex(slot, this.content.length, "Slot number");
        Slot content = this.content[slot];
        content.setItemStack(itemStack);
        content.setClickHandler(clickHandler);
    }

    @Override
    public void addItem(ItemStack itemStack, ClickHandler clickHandler) {
        for (Slot content : this.content) {
            if (!content.isEmpty()) continue;
            content.setItemStack(itemStack);
            content.setClickHandler(clickHandler);
            break;
        }
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        Preconditions.checkElementIndex(slot, this.content.length, "Slot number");
        this.content[slot].setItemStack(itemStack);
    }

    @Override
    public void setClickHandler(int slot, ClickHandler clickHandler) {
        Preconditions.checkElementIndex(slot, this.content.length, "Slot number");
        this.content[slot].setClickHandler(clickHandler);
    }

    @Override
    public void fillPattern(ItemStack itemStack, InventoryPattern... patterns) {
        for (InventoryPattern pattern : patterns) {
            for (int slot : pattern.getSlots(this.type, this.size)) {
                setItem(slot, itemStack);
            }
        }
    }

}
