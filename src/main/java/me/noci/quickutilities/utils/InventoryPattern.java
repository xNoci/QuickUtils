package me.noci.quickutilities.utils;

import com.google.common.base.Predicates;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.Range;

import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public enum InventoryPattern {

    TOP {
        int[] matchInventory(int size) { return x(size, i -> i < 9); }
        int[] matchDispenser(int size) { return x(size, i -> i < 3); }
    },
    BOTTOM{
        int[] matchInventory(int size) { return x(size, i -> i > size - 10); }
        int[] matchDispenser(int size) { return x(size, i -> i > size - 4); }
    },
    LEFT {
        int[] matchInventory(int size) { return x(size, i -> i % 9 == 0); }
        int[] matchDispenser(int size) { return x(size, i -> i % 3 == 0); }
    },
    RIGHT {
        int[] matchInventory(int size) { return x(size, i -> (i + 1) % 9 == 0); }
        int[] matchDispenser(int size) { return x(size, i -> (i + 1) % 3 == 0); }
    },
    TOP_LEFT_CORNER {
        int[] matchInventory(int size) { return x(size, i -> i < 2 || i == 9); }
        int[] matchDispenser(int size) { return x(size, i -> i == 0); }
    },
    TOP_RIGHT_CORNER {
        int[] matchInventory(int size) { return x(size, i -> i == 7 || i == 8 || i == 17); }
        int[] matchDispenser(int size) { return x(size, i -> i == 2); }
    },
    BOTTOM_LEFT_CORNER {
        int[] matchInventory(int size) { return x(size, i -> i == size - 18 || (i > size - 10 && i < size - 7)); }
        int[] matchDispenser(int size) { return x(size, i -> i == 6); }
    },
    BOTTOM_RIGHT_CORNER {
        int[] matchInventory(int size) { return x(size, i -> i == size - 10 || i > size - 3); }
        int[] matchDispenser(int size) { return x(size, i -> i == 8); }
    },
    FULL {
        int[] matchInventory(int size) { return x(size, i -> true); }
        int[] matchDispenser(int size) { return x(size, i -> true); }
        int[] matchDefault(int size) { return x(size, i -> true);}
    },
    CENTER {
        int[] matchInventory(int size) { return x(size, i -> (size <= 18 ||  i > 9 && i < size - 10) && i % 9 != 0 && (i + 1) % 9 != 0); }
        int[] matchDispenser(int size) { return x(size, i -> i == 4); }
    };

    private static int[] x(int size, IntPredicate predicate) {
        return IntStream.range(0, size).filter(predicate).toArray();
    }

    public static int[] box(@Range(from = 1, to = 6) int firstRow, @Range(from = 1, to = 6) int lastRow) {
        int minSlot = 9 * (firstRow - 1);
        int maxSlot = 9 * (lastRow - 1) + 8;
        return IntStream.range(0, 54)
                .filter(i -> i % 9 != 0)
                .filter(i -> (i + 1) % 9 != 0)
                .filter(i -> i >= minSlot && i <= maxSlot)
                .toArray();
    }

    public static int[] range(@Range(from = 1, to = 54) int firstSlot, @Range(from = 1, to = 54) int lastSlot) {
        int first = firstSlot - 1;
        int second = lastSlot - 1;
        return IntStream.range(0, 54).filter(i -> i >= first && i <= second).toArray();
    }

    public int[] getDispenserSlots(int size) {
        return getSlots(InventoryType.DISPENSER, size);
    }

    public int[] getSlots(int size) {
        return getSlots(InventoryType.CHEST, size);
    }

    public int[] getSlots(InventoryType type, int size) {
        return match(type, size);
    }

    private int[] match(InventoryType type, int size) {
        return switch (type) {
            case CHEST, ENDER_CHEST -> matchInventory(size);
            case DROPPER, DISPENSER -> matchDispenser(size);
            default -> matchDefault(size);
        };
    }

    //9*x inventories
    abstract int[] matchInventory(int size);

    //3*3 inventories
    abstract int[] matchDispenser(int size);

    //all other kind of inventories
    int[] matchDefault(int size) {
        return new int[0];
    }

}
