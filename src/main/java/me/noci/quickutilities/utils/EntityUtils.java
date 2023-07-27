package me.noci.quickutilities.utils;

import me.noci.quickutilities.QuickUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityUtils {

    public static void removePotionEffects(LivingEntity entity) {
        for (PotionEffect effect : entity.getActivePotionEffects()) {
            entity.removePotionEffect(effect.getType());
        }
    }

    public static void clearChat(Player player) {
        for (int i = 0; i < 500; i++) {
            player.sendMessage("");
        }
    }

    public static void respawn(Player player, int delay, BukkitUnit timeUnit) {
        long ticks = timeUnit.toTicks(delay);
        if (ticks <= 0) {
            player.spigot().respawn();
            return;
        }

        Bukkit.getScheduler().runTaskLater(QuickUtils.instance(), () -> player.spigot().respawn(), ticks);
    }

    public static void setLevelProgress(Player player, int value, int max) {
        setLevelProgress(player, value, 0, max);
    } 

    public static void setLevelProgress(Player player, int value, int min, int max) {
        Require.checkArgument(value >= 0, "Value has to be greater or equal to zero");
        Require.checkState(max > min, "Max (%s) has to be greater than min (%s)", min, max);
        Require.checkState(max >= value && value >= min, "Value (%s) has to be between max (%s) and min (%s)", value, max, min);
        player.setLevel(value);
        player.setExp(MathUtils.clamp((float) (value - min) / (float) (max - min), 0.01f, 0.99f));
    }

    public static void resetLevel(Player player) {
        player.setLevel(0);
        player.setExp(0);
    }

    public static List<Player> nearbyPlayers(Entity rootEntity, int distance) {
        return nearbyEntities(rootEntity, distance, Player.class);
    }

    public static List<Entity> nearbyEntities(Entity rootEntity, int distance) {
        return nearbyEntities(rootEntity, distance, Entity.class);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> List<T> nearbyEntities(Entity rootEntity, int distance, Class<T> targetType) {
        int squaredDistance = distance * distance;
        Location rootLocation = rootEntity.getLocation();
        return rootLocation.getWorld().getEntities().stream()
                .filter(entity -> !entity.getUniqueId().equals(rootEntity.getUniqueId()))
                .filter(entity -> targetType.isAssignableFrom(entity.getClass()))
                .filter(entity -> entity.getLocation().distanceSquared(rootLocation) <= squaredDistance)
                .map(entity -> (T) entity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Player> nearbyPlayers(Location rootLocation, int distance) {
        return nearbyEntities(rootLocation, distance, Player.class);
    }

    public static List<Entity> nearbyEntities(Location rootLocation, int distance) {
        return nearbyEntities(rootLocation, distance, Entity.class);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> List<T> nearbyEntities(Location rootLocation, int distance, Class<T> targetType) {
        int squaredDistance = distance * distance;
        return rootLocation.getWorld().getEntities().stream()
                .filter(entity -> targetType.isAssignableFrom(entity.getClass()))
                .filter(entity -> entity.getLocation().distanceSquared(rootLocation) <= squaredDistance)
                .map(entity -> (T) entity)
                .collect(Collectors.toCollection(ArrayList::new));

    }

}
