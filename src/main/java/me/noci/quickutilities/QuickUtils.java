package me.noci.quickutilities;

import me.noci.quickutilities.inventory.GuiManager;
import me.noci.quickutilities.quicktab.QuickTab;
import me.noci.quickutilities.quicktab.builder.QuickTabBuilder;
import me.noci.quickutilities.quicktab.utils.CollisionRule;
import me.noci.quickutilities.quicktab.utils.NameTagVisibility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class QuickUtils extends JavaPlugin implements Listener {

    private static QuickUtils instance;
    PageTestInventory pageTestInventory = new PageTestInventory();
    //NON-Player specific
    TestInventory inventory = new TestInventory();

    public static QuickUtils instance() {
        return instance;
    }

    //Player specific
    public TestInventory getInventory(Player player) {
        return new TestInventory(player);
    }

    @Override
    public void onEnable() {
        instance = this;

        new GuiManager(this);

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(this, this);

        QuickTabBuilder tabBuilder = QuickTab.builder()
                .prefix((player, target) -> "§7Test §8| ")
                .suffix((player, target) -> " §f[-]")
                .collisionRule((player, target) -> CollisionRule.NEVER)
                .seeFriendlyInvisible((player, target) -> true)
                .nameTagVisibility((player, target) -> NameTagVisibility.HIDE_FOR_OWN_TEAM)
                .color((player, target) -> ChatColor.RED);
        QuickTab.setUpdatingTabList(this, tabBuilder);
    }


    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            pageTestInventory.provide(event.getPlayer());
        }, 20);
    }

}
