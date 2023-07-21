package me.noci.quickutilities.quickcommand.mappings;

import org.bukkit.GameMode;
import org.jetbrains.annotations.Nullable;

public class MappingFunctions {

    public static @Nullable GameMode parseGameMode(String value) {
        return switch (value.toLowerCase()) {
            case "0", "survival" -> GameMode.SURVIVAL;
            case "1", "creative" -> GameMode.CREATIVE;
            case "2", "adventure" -> GameMode.ADVENTURE;
            case "3", "spec", "spectator" -> GameMode.SPECTATOR;
            default -> null;
        };
    }

}
