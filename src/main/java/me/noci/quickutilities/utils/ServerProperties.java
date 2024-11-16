package me.noci.quickutilities.utils;

import com.cryptomorin.xseries.reflection.XReflection;
import lombok.SneakyThrows;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;

import java.io.FileInputStream;
import java.util.Properties;

public class ServerProperties {

    private final Properties properties = new Properties();

    @SneakyThrows
    private ServerProperties() {
        try (FileInputStream inputStream = new FileInputStream("server.properties")) {
            properties.load(inputStream);
        }
    }

    private static class SingletonHelper {
        private static final ServerProperties INSTANCE = new ServerProperties();
    }

    private static ServerProperties instance() {
        return SingletonHelper.INSTANCE;
    }

    public static String getSeed() {
        return instance().getString("level-seed");
    }

    public static boolean isCommandBlockEnabled() {
        return instance().getBoolean("enable-command-block");
    }

    public static GameMode getGamemode() {
        return XReflection.v(14, GameMode.valueOf(instance().getString("gamemode"))).orElse(GameMode.values()[instance().getInt("gamemode") % GameMode.values().length]);
    }

    public static String getDefaultWorld() {
        return instance().getString("level-name");
    }

    public static String getMotd() {
        return instance().getString("motd");
    }

    public static boolean isPvP() {
        return instance().getBoolean("pvp");
    }

    public static Difficulty getDifficulty() {
        return XReflection.v(14, Difficulty.valueOf(instance().getString("difficulty"))).orElse(Difficulty.values()[instance().getInt("difficulty") % Difficulty.values().length]);
    }

    public static boolean isSpawnAnimals() {
        return instance().getBoolean("spawn-animals");
    }

    public static boolean isHardcore() {
        return instance().getBoolean("hardcore");
    }

    public static boolean isSpawnMonsters() {
        return instance().getBoolean("spawn-monsters");
    }

    public static int getSpawnProtection() {
        return instance().getInt("spawn-protection");
    }

    public static boolean isSpawnNpcs() {
        return instance().getBoolean("spawn-npcs");
    }

    public static String getResourcePack() {
        return instance().getString("resource-pack");
    }

    public static int getOpPermissionLevel() {
        return instance().getInt("op-permission-level");
    }

    public static String getServerName() {
        return instance().getString("server-name");
    }

    private boolean getBoolean(String key) {
        return Boolean.parseBoolean(instance().properties.getProperty(key));
    }


    private int getInt(String key) {
        return Integer.parseInt(instance().properties.getProperty(key));
    }

    private String getString(String key) {
        return instance().properties.getProperty(key);
    }

}
