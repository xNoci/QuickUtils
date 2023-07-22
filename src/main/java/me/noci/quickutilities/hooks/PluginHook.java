package me.noci.quickutilities.hooks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public abstract class PluginHook {

    private final Plugin plugin;

    protected PluginHook(String pluginName) {
        this.plugin = Bukkit.getPluginManager().getPlugin(pluginName);
    }

    public boolean enabled() {
        return plugin != null && plugin.isEnabled();
    }

}
