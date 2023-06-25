package me.noci.quickutilities.input;

import com.cryptomorin.xseries.messages.Titles;
import me.noci.quickutilities.utils.BukkitUnit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class TitledPlayerChatInput extends PlayerChatInput {

    //TODO Add constructor parameter
    private final boolean fadeOut = false;
    private final int fadeOutTime = (int) (BukkitUnit.SECONDS.toTicks(1) / 2);

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
                if (!isInputMode()) {
                    //TODO TEST
                    if (fadeOut) {
                        Titles.sendTitle(player, 0, 10, fadeOutTime, title, subtitle);
                    } else {
                        Titles.clearTitle(player);
                    }
                    cancel();
                    return;
                }

                Titles.sendTitle(player, title, subtitle);
            }
        }.runTaskTimerAsynchronously(plugin, 0, 10);

    }
}
