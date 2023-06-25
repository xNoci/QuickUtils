package me.noci.quickutilities.input;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AbstractPlayerInput implements Listener {

    protected Player player;
    protected Consumer<String> playerInput;
    @Getter protected boolean inputMode = true;
    private BiConsumer<Player, Boolean> onCancel;

    public AbstractPlayerInput(JavaPlugin plugin, Player player, Consumer<String> playerInput) {
        this.player = player;
        this.playerInput = playerInput;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void onCancel(BiConsumer<Player, Boolean> task) {
        this.onCancel = task;
    }

    @EventHandler
    protected void handlePlayerQuit(PlayerQuitEvent event) {
        cancel(true);
    }

    protected void cancel(boolean forced) {
        HandlerList.unregisterAll(this);

        if (onCancel != null && player.isOnline()) {
            onCancel.accept(player, forced);
        }

        this.inputMode = false;
        this.player = null;
        this.playerInput = null;
    }

}
