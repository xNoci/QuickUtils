package me.noci.quickutilities.utils;

import java.util.stream.IntStream;

public enum InventoryPattern {

    TOP(size -> IntStream.range(0, size).filter(i -> i < 9).toArray()),
    BOTTOM(size -> IntStream.range(0, size).filter(i -> i > size - 10).toArray()),
    LEFT(size -> IntStream.range(0, size).filter(i -> i % 9 == 0).toArray()),
    RIGHT(size -> IntStream.range(0, size).filter(i -> (i + 1) % 9 == 0).toArray()),
    TOP_LEFT_CORNER(size -> IntStream.range(0, size).filter(i -> i < 2 || i == 9).toArray()),
    TOP_RIGHT_CORNER(size -> IntStream.range(0, size).filter(i -> i == 7 || i == 8 || i == 17).toArray()),
    BOTTOM_LEFT_CORNER(size -> IntStream.range(0, size).filter(i -> i == size - 18 || (i > size - 10 && i < size - 7)).toArray()),
    BOTTOM_RIGHT_CORNER(size -> IntStream.range(0, size).filter(i -> i == size - 10 || i > size - 3).toArray()),
    FULL(size -> IntStream.range(0, size).toArray());

    private final SlotMatcher matcher;

    InventoryPattern(SlotMatcher matcher) {
        this.matcher = matcher;
    }

    public int[] getSlots(int size) {
        return matcher.match(size);
    }

    @FunctionalInterface
    private interface SlotMatcher {

        int[] match(int size);

    }

}
