package me.noci.quickutilities.utils;

import org.bukkit.event.inventory.InventoryType;

import java.util.function.IntPredicate;
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

    public int[] getSlots(InventoryType type, int size) {
        return match(type, size);
    }

    private int[] match(InventoryType type, int size) {
        switch (type) {
            case CHEST:
            case ENDER_CHEST:
                return matchInventory(size);
            case DROPPER:
            case DISPENSER:
                return matchDispenser(size);
        }

        return matchDefault(size);
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
