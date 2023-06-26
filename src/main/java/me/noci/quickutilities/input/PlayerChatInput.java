package me.noci.quickutilities.input;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerChatInput extends BasePlayerInput implements Listener {

    protected static final String CANCEL_STRING = "!cancel";
    private final String cancelString;

    public PlayerChatInput(JavaPlugin plugin, Player player, String notifyMessage, InputExecutor inputExecutor) {
        this(plugin, player, notifyMessage, CANCEL_STRING, inputExecutor);
    }

    public PlayerChatInput(JavaPlugin plugin, Player player, String notifyMessage, String cancelString, InputExecutor inputExecutor) {
        super(player, inputExecutor);
        this.cancelString = cancelString;
        player.sendMessage(notifyMessage);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    protected void handlePlayerQuit(PlayerQuitEvent event) {
        stopInput(true);
    }

    @EventHandler
    protected void handlePlayerChat(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().getUniqueId().equals(player.getUniqueId())) return;
        event.setCancelled(true);

        String message = event.getMessage();
        if (message.equals(cancelString)) {
            stopInput(true);
            return;
        }

        inputExecutor.execute(message);
        stopInput(false);
    }

    @Override
    public void cleanUp() {
        HandlerList.unregisterAll(this);
    }
}
