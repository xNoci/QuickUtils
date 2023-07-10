package me.noci.quickutilities.utils;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class DirectionUtils {

    public static BlockFace getPlayerDirection(Player player) {
        float yaw = player.getLocation().getYaw();
        if (yaw < 0) yaw += 360;
        yaw %= 360;

        int i = (int) ((yaw + 8) / 22.5);

        return switch (i) {
            case 1 -> BlockFace.SOUTH_SOUTH_WEST;
            case 2 -> BlockFace.SOUTH_WEST;
            case 3 -> BlockFace.WEST_SOUTH_WEST;
            case 4 -> BlockFace.WEST;
            case 5 -> BlockFace.WEST_NORTH_WEST;
            case 6 -> BlockFace.NORTH_WEST;
            case 7 -> BlockFace.NORTH_NORTH_WEST;
            case 8 -> BlockFace.NORTH;
            case 9 -> BlockFace.NORTH_NORTH_EAST;
            case 10 -> BlockFace.NORTH_EAST;
            case 11 -> BlockFace.EAST_NORTH_EAST;
            case 12 -> BlockFace.EAST;
            case 13 -> BlockFace.EAST_SOUTH_EAST;
            case 14 -> BlockFace.SOUTH_EAST;
            case 15 -> BlockFace.SOUTH_SOUTH_EAST;
            default -> BlockFace.SOUTH;
        };
    }

    public static BlockFace getBlockDirection(Player player, boolean allowDownUp) {
        if (allowDownUp) {
            float pitch = player.getLocation().getPitch();
            if (pitch <= -45) return BlockFace.DOWN;
            if (pitch >= 45) return BlockFace.UP;
        }

        return getCardinalDirection(player);
    }

    public static BlockFace getCardinalDirection(Player player) {
        BlockFace blockFace = getPlayerDirection(player);

        return switch (blockFace) {
            case WEST_SOUTH_WEST, WEST, WEST_NORTH_WEST, NORTH_WEST -> BlockFace.EAST;
            case NORTH_NORTH_WEST, NORTH, NORTH_NORTH_EAST, NORTH_EAST -> BlockFace.SOUTH;
            case EAST_NORTH_EAST, EAST, EAST_SOUTH_EAST, SOUTH_EAST -> BlockFace.WEST;
            default -> BlockFace.NORTH;
        };
    }

}
