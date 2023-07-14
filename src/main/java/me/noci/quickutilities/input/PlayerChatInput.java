package me.noci.quickutilities.input;

import me.noci.quickutilities.QuickUtils;
import me.noci.quickutilities.input.functions.InputExecutor;
import org.bukkit.Bukkit;
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

    protected PlayerChatInput(Player player, String notifyMessage, InputExecutor inputExecutor) {
        this(player, notifyMessage, CANCEL_STRING, inputExecutor);
    }

    protected PlayerChatInput(Player player, String notifyMessage, String cancelString, InputExecutor inputExecutor) {
        super(player, inputExecutor);
        this.cancelString = cancelString;
        player.sendMessage(notifyMessage);
        Bukkit.getServer().getPluginManager().registerEvents(this, QuickUtils.instance());
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
