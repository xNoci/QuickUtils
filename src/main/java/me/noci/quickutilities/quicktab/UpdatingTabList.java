package me.noci.quickutilities.quicktab;

import me.noci.quickutilities.quicktab.builder.QuickTabBuilder;
import me.noci.quickutilities.utils.Require;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class UpdatingTabList implements Listener {

    private final QuickTabBuilder builder;

    protected UpdatingTabList(JavaPlugin plugin, QuickTabBuilder builder) {
        Require.checkState(builder != null, "TabListTeamBuilder cannot be null.");
        this.builder = builder;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void delete() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void handlePlayerJoin(PlayerJoinEvent event) {
        update();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void handlePlayerQuit(PlayerQuitEvent event) {
        update();
    }

    public void update(Player player) {
        QuickTab.internalUpdate(player, builder);
    }

    public void update() {
        Bukkit.getOnlinePlayers().forEach(player -> QuickTab.internalUpdate(player, builder));
    }

}
