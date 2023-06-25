package me.noci.quickutilities.input;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class PlayerChatInput extends AbstractPlayerInput {

    protected static final String CANCEL_STRING = "!cancel";

    private final String cancelString;

    public PlayerChatInput(JavaPlugin plugin, Player player, Consumer<String> playerInput) {
        this(plugin, player, CANCEL_STRING, playerInput);
    }

    public PlayerChatInput(JavaPlugin plugin, Player player, String cancelString, Consumer<String> playerInput) {
        super(plugin, player, playerInput);
        this.cancelString = cancelString;
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

}
