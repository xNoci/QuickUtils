package me.noci.quickutilities.inventory;

import com.google.common.base.Preconditions;
import lombok.Getter;
import me.noci.quickutilities.utils.InventoryPattern;
import me.noci.quickutilities.utils.Require;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class GuiHolder implements InventoryHolder, InventoryContent {

    static {
        GuiManager.initialise();
    }

    @Getter private final QuickGUIProvider provider;
    @Getter private PageContent pageContent = null;
    private final Inventory handle;

    private final InventoryType type;
    private final int size;
    private final Slot[] slots;

    public GuiHolder(QuickGUIProvider provider, int size, Component title) {
        this(provider, InventoryType.CHEST, size, title);
    }

    public GuiHolder(QuickGUIProvider provider, InventoryType type, Component title) {
        this(provider, type, type.getDefaultSize(), title);
    }

    public GuiHolder(QuickGUIProvider provider, InventoryType type, int size, Component title) {
        Require.nonNull(provider, "QuickGUIProvider cannot be null");

        this.provider = provider;
        this.type = type;
        this.size = size;
        this.slots = new Slot[this.size];
        Arrays.setAll(slots, i -> new Slot(new SlotPos(type, i)));

        if (type == InventoryType.CHEST && size > 0) {
            handle = Bukkit.createInventory(this, size, title);
        } else {
            //Change dropper inventory to dispenser due to wierd IndexOutOfBoundException thrown by minecraft
            handle = Bukkit.createInventory(this, type == InventoryType.DROPPER ? InventoryType.DISPENSER : type, title);
        }
    }

    protected void setPageContent(PageContent pageContent) {
        if (this.pageContent != null) return;
        this.pageContent = pageContent;
        this.pageContent.setHandle(this);
    }

    public void applyContent() {
        this.handle.clear();
        for (int i = 0; i < this.handle.getSize(); i++) {
            this.handle.setItem(i, getSlot(i).getItemStack());
        }
    }

    @Override
    @NotNull
    public Inventory getInventory() {
        return this.handle;
    }

    @Override
    public Slot getSlot(int slot) {
        Preconditions.checkElementIndex(slot, slots.length, "Slot index");
        return slots[slot];
    }

    @Override
    public void setItem(int slot, GuiItem item) {
        Preconditions.checkElementIndex(slot, slots.length, "Slot number");
        slots[slot].setItem(item);
    }

    @Override
    public void addItem(GuiItem item) {
        Arrays.stream(slots)
                .filter(Slot::isEmpty)
                .findFirst()
                .ifPresent(slot -> slot.setItem(item));
    }

    @Override
    public void clearItem(int slot) {
        Preconditions.checkElementIndex(slot, this.slots.length, "Slot number");
        slots[slot].setItem(GuiItem.empty());
    }

    @Override
    public void fillPattern(GuiItem item, InventoryPattern... patterns) {
        Arrays.stream(patterns)
                .flatMapToInt(pattern -> Arrays.stream(pattern.getSlots(this.type, this.size)))
                .distinct()
                .forEach(slot -> setItem(slot, item));
    }

    @Override
    public void fillSlots(GuiItem item, int[] slots) {
        Arrays.stream(slots).forEach(slot -> setItem(slot, item));
    }

    public boolean hasPageContent() {
        return pageContent != null;
    }
}
