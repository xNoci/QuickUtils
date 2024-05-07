package me.noci.quickutilities.inventory;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Setter;
import me.noci.quickutilities.utils.InventoryPattern;
import org.bukkit.event.inventory.InventoryType;

public class DefaultInventoryContent implements InventoryContent {

    //TODO Merge this into GuiHolder

    private final InventoryType type;
    private final int size;
    private final Slot[] content;
    private final GuiHolder guiHolder;

    protected DefaultInventoryContent(GuiHolder holder, InventoryType type, int size) {
        this.type = type;
        this.size = size;
        this.content = new Slot[size];
        this.guiHolder = holder;

        for (int i = 0; i < size; i++) {
            SlotPos slotPos = new SlotPos(type, i);
            this.content[i] = new Slot(slotPos);
        }
    }


    @Override
    public void applyContent() {
       guiHolder.applyContent();
    }

    @Override
    public Slot getSlot(int slot) {
        if (slot > this.content.length) return null;
        return this.content[slot];
    }

    @Override
    public void setItem(int slot, GuiItem item) {
        Preconditions.checkElementIndex(slot, this.content.length, "Slot number");
        Slot content = this.content[slot];
        content.setItem(item);
    }

    @Override
    public void addItem(GuiItem item) {
        for (Slot content : this.content) {
            if (!content.isEmpty()) continue;
            content.setItem(item);
            break;
        }
    }

    @Override
    public void clearItem(int slot) {
        Preconditions.checkElementIndex(slot, this.content.length, "Slot number");
        Slot content = this.content[slot];
        content.setItem(GuiItem.empty());
    }

    @Override
    public void fillPattern(GuiItem item, InventoryPattern... patterns) {
        for (InventoryPattern pattern : patterns) {
            for (int slot : pattern.getSlots(this.type, this.size)) {
                setItem(slot, item);
            }
        }
    }

    @Override
    public void fillSlots(GuiItem item, int[] slots) {
        for (int slot : slots) {
            setItem(slot, item);
        }
    }

}
