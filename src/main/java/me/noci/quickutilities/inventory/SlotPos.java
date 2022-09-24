package me.noci.quickutilities.inventory;

import lombok.Getter;
import org.bukkit.event.inventory.InventoryType;

public class SlotPos {

    @Getter private final int slot;

    @Getter private final int row;
    @Getter private final int colum;

    public SlotPos(InventoryType type, int slot) {
        this.slot = slot;

        switch (type) {
            case CHEST:
            case ENDER_CHEST:
                row = slot / 9;
                colum = slot % 9;
                break;
            case DISPENSER:
            case DROPPER:
                row = slot / 3;
                colum = slot % 3;
                break;
            case HOPPER:
                row = 0;
                colum = slot % 5;
                break;
            default:
                row = -1;
                colum = -1;
                break;
        }

    }

    @Override
    public String toString() {
        return "SlotPos{" +
                "slot=" + slot +
                ", row=" + row +
                ", colum=" + colum +
                '}';
    }

}
