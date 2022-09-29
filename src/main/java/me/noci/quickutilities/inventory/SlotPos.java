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
            case CHEST, ENDER_CHEST -> {
                row = slot / 9;
                colum = slot % 9;
            }
            case DISPENSER, DROPPER -> {
                row = slot / 3;
                colum = slot % 3;
            }
            case HOPPER -> {
                row = 0;
                colum = slot % 5;
            }
            default -> {
                row = -1;
                colum = -1;
            }
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
