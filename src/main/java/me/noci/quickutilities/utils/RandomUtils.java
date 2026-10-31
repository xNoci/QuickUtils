package me.noci.quickutilities.utils;

import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Arrays;
import java.util.List;
import java.util.SplittableRandom;
import java.util.random.RandomGenerator;

import static net.kyori.adventure.text.format.NamedTextColor.*;

public class RandomUtils {
    static final NamedTextColor[] VALUES = new NamedTextColor[]{BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED,
            DARK_PURPLE,
            GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE};

    public static boolean chance(double chance) {
        return !(chance <= 0) && RandomHolder.RANDOM.nextDouble() <= chance;
    }

    public static NamedTextColor color() {
        return VALUES[RandomHolder.RANDOM.nextInt(16)];
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public static NamedTextColor color(NamedTextColor... exclude) {
        List<NamedTextColor> excluded = Arrays.asList(exclude);
        NamedTextColor color;
        while (excluded.contains(color = color())) ;
        return color;
    }

    public static RandomGenerator random() {
        return RandomHolder.RANDOM;
    }

    private static class RandomHolder {
        private static final RandomGenerator RANDOM = new SplittableRandom();
    }

}
