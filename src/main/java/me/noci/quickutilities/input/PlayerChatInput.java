package me.noci.quickutilities.input;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class PlayerChatInput extends BasePlayerInput implements Listener {

    protected static final String CANCEL_STRING = "!cancel";
    private final String cancelString;

    public PlayerChatInput(JavaPlugin plugin, Player player, String notifyMessage, Consumer<String> playerInput) {
        this(plugin, player, notifyMessage, CANCEL_STRING, playerInput);
    }

    public PlayerChatInput(JavaPlugin plugin, Player player, String notifyMessage, String cancelString, Consumer<String> playerInput) {
        super(player, playerInput);
        this.cancelString = cancelString;
        player.sendMessage(notifyMessage);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    protected void handlePlayerQuit(PlayerQuitEvent event) {
        cancel(true);
    }

    @EventHandler
    protected void handlePlayerChat(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().getUniqueId().equals(player.getUniqueId())) return;
        event.setCancelled(true);

        String message = event.getMessage();
        if (message.equals(cancelString)) {
            cancel(true);
            return;
        }

        playerInput.accept(message);
        cancel(false);
    }

    @Override
    public void cleanUp() {
        HandlerList.unregisterAll(this);
    }
}
