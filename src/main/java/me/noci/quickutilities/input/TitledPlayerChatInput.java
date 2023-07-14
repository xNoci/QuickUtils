package me.noci.quickutilities.input;

import com.cryptomorin.xseries.messages.Titles;
import me.noci.quickutilities.input.functions.InputExecutor;
import me.noci.quickutilities.utils.BukkitUnit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TitledPlayerChatInput extends BasePlayerInput implements Listener {

    protected static final String CANCEL_STRING = "!cancel";
    private final String cancelString;

    //TODO Add constructor parameter
    private final boolean fadeOut = false;
    private final int fadeOutTime = (int) (BukkitUnit.SECONDS.toTicks(1) / 2);

    protected TitledPlayerChatInput(JavaPlugin plugin, Player player, InputExecutor inputExecutor, String title) {
        this(plugin, player, inputExecutor, title, "§7Type '§c§o%s§r§7' to abort.".formatted(CANCEL_STRING));
    }

    protected TitledPlayerChatInput(JavaPlugin plugin, Player player, InputExecutor inputExecutor, String title, String subtitle) {
        this(plugin, player, CANCEL_STRING, inputExecutor, title, subtitle);
    }

    protected TitledPlayerChatInput(JavaPlugin plugin, Player player, String cancelString, InputExecutor inputExecutor, String title, String subtitle) {
        super(player, inputExecutor);
        this.cancelString = cancelString;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
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

                Titles.sendTitle(player, 0, 20, 0, title, subtitle);
            }
        }.runTaskTimerAsynchronously(plugin, 0, 10);

    }

    @Override
    public void cleanUp() {
        HandlerList.unregisterAll(this);
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

}
