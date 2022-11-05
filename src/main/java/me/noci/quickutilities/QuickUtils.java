package me.noci.quickutilities;

import me.noci.quickutilities.inventory.GuiManager;
import me.noci.quickutilities.listener.EntityDamageByEntityListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class QuickUtils extends JavaPlugin implements Listener {

    private static QuickUtils instance;

    public static QuickUtils instance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        new GuiManager(this);

        registerListener();
    }

    private void registerListener() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new EntityDamageByEntityListener(), this);
    }

}
