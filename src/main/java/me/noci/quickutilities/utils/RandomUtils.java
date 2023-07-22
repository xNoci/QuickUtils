package me.noci.quickutilities.utils;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;
import java.util.SplittableRandom;
import java.util.random.RandomGenerator;

public class RandomUtils {

    public static boolean chance(double chance) {
        return !(chance <= 0) && RandomHolder.RANDOM.nextDouble() <= chance;
    }

    public static ChatColor color() {
        return ChatColor.values()[RandomHolder.RANDOM.nextInt(16)];
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public static ChatColor color(ChatColor... exclude) {
        List<ChatColor> excluded = Arrays.asList(exclude);
        ChatColor color;
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
