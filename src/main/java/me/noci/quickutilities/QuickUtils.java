package me.noci.quickutilities;

import me.noci.quickutilities.inventory.GuiManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class QuickUtils extends JavaPlugin implements Listener {

    TestInventory inventory = new TestInventory();

    @Override
    public void onEnable() {
        new GuiManager(this);

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(this, this);
    }


    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            inventory.provide(event.getPlayer());
        }, 20);
    }

}
