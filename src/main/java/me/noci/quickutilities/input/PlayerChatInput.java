package me.noci.quickutilities.input;

import me.noci.quickutilities.input.functions.InputExecutor;
import org.bukkit.entity.Player;

public class PlayerChatInput extends BaseChatInput {

    protected PlayerChatInput(Player player, String notifyMessage, InputExecutor inputExecutor) {
        this(player, notifyMessage, CANCEL_STRING, inputExecutor);
    }

    protected PlayerChatInput(Player player, String notifyMessage, String cancelString, InputExecutor inputExecutor) {
        super(player, cancelString, inputExecutor);
        player.sendMessage(notifyMessage);
    }

}
