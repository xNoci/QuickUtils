package me.noci.quickutilities;

import me.noci.quickutilities.listener.QuickGuiListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class QuickUtils extends JavaPlugin {

    @Override
    public void onEnable() {
        
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new QuickGuiListener(), this);

    }
}
