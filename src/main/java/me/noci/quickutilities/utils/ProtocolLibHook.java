package me.noci.quickutilities.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ProtocolLibHook {

    private static Plugin plugin;
    private static boolean checktPlugin = false;

    public static boolean isEnabled() {
        if (!checktPlugin) {
            checktPlugin = true;
            plugin = Bukkit.getPluginManager().getPlugin("ProtocolLib");
        }
        if (plugin == null) return false;
        return plugin.isEnabled();
    }

}
