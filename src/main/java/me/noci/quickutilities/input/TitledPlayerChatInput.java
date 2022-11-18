package me.noci.quickutilities.input;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class TitledPlayerChatInput extends PlayerChatInput {


    public TitledPlayerChatInput(JavaPlugin plugin, Player player, Consumer<String> playerInput, String title) {
        this(plugin, player, playerInput, title, "§7Type '§c§o%s§r§7' to abort.".formatted(CANCEL_STRING));
    }

    public TitledPlayerChatInput(JavaPlugin plugin, Player player, Consumer<String> playerInput, String title, String subtitle) {
        this(plugin, player, CANCEL_STRING, playerInput, title, subtitle);
    }

    public TitledPlayerChatInput(JavaPlugin plugin, Player player, String cancelString, Consumer<String> playerInput, String title, String subtitle) {
        super(plugin, player, cancelString, playerInput);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!isInputMode()) {
                    //TODO FADE OUT
                    player.sendTitle(" ", " "); /*TODO USING REFLECTION*/
                    cancel();
                    return;
                }

                //TODO SEND CONSTANTLY
                player.sendTitle(title, subtitle); /*TODO USING REFLECTION*/
            }
        }.runTaskTimerAsynchronously(plugin, 0, 10);

    }
}
