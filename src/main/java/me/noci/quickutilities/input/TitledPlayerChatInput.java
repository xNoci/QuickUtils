package me.noci.quickutilities.input;

import com.cryptomorin.xseries.messages.Titles;
import me.noci.quickutilities.input.functions.InputExecutor;
import me.noci.quickutilities.utils.BukkitUnit;
import me.noci.quickutilities.utils.Scheduler;
import org.bukkit.entity.Player;

public class TitledPlayerChatInput extends BaseChatInput {

    //TODO Add constructor parameter
    private final boolean fadeOut = false;
    private final int fadeOutTime = (int) (BukkitUnit.SECONDS.toTicks(1) / 2);

    protected TitledPlayerChatInput(Player player, InputExecutor inputExecutor, String title) {
        this(player, inputExecutor, title, "§7Type '§c§o%s§r§7' to abort.".formatted(CANCEL_STRING));
    }

    protected TitledPlayerChatInput(Player player, InputExecutor inputExecutor, String title, String subtitle) {
        this(player, CANCEL_STRING, inputExecutor, title, subtitle);
    }

    protected TitledPlayerChatInput(Player player, String cancelString, InputExecutor inputExecutor, String title, String subtitle) {
        super(player, cancelString, inputExecutor);

        Scheduler.repeat(10, runnable -> {
            if (!isInputMode()) {
                if (fadeOut) {
                    Titles.sendTitle(player, 0, 10, fadeOutTime, title, subtitle);
                } else {
                    Titles.clearTitle(player);
                }
                runnable.cancel();
                return;
            }
            Titles.sendTitle(player, 0, 20, 0, title, subtitle);
        });
    }
}
