package me.noci.quickutilities.inventory;

import lombok.Getter;
import org.bukkit.event.inventory.InventoryType;

public class SlotPos {

    @Getter private final int slot;

    @Getter private final int row = 0;
    @Getter private final int colum = 0;

    public SlotPos(InventoryType type, int slot) {
        this.slot = slot;

        //TODO calculate row and colum by type

    }

}
