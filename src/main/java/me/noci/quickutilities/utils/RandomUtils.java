package me.noci.quickutilities.utils;

import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.random.RandomGenerator;

public class RandomUtils {

    public static boolean chance(double chance) {
        if (chance <= 0)
            return false;
        return (RandomHolder.RANDOM.nextDouble() <= chance);
    }

    public static boolean fiftyFifty() {
        return RandomHolder.RANDOM.nextBoolean();
    }

    public static ChatColor randomChatColor() {
        return ChatColor.values()[RandomHolder.RANDOM.nextInt(16)];
    }

    public static ChatColor randomChatColor(ChatColor... exclude) {
        ChatColor newColor;
        do {
            newColor = randomChatColor();
        } while (Arrays.asList(exclude).contains(newColor));
        return newColor;
    }

    private static class RandomHolder {
        private static final RandomGenerator RANDOM = RandomGenerator.getDefault();
    }


}
