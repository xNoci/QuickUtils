package me.noci.quickutilities.input;

import me.noci.quickutilities.events.Events;
import me.noci.quickutilities.events.subscriber.SubscribedEvent;
import me.noci.quickutilities.input.functions.InputExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public abstract class BaseChatInput extends BasePlayerInput {

    protected static final String CANCEL_STRING = "!cancel";

    private SubscribedEvent<AsyncPlayerChatEvent> chatEvent;

    protected BaseChatInput(Player player, String cancelString, InputExecutor inputExecutor) {
        super(player, inputExecutor);
        chatEvent = Events.subscribe(AsyncPlayerChatEvent.class)
                .expireAfter(1)
                .filter(event -> event.getPlayer().getUniqueId().equals(player.getUniqueId()))
                .handle(event -> {
                    event.setCancelled(true);
                    String message = event.getMessage();
                    if (message.equals(cancelString)) {
                        stopInput(true);
                        return;
                    }

                    inputExecutor.execute(message);
                    stopInput(false);
                });

    }

    @Override
    public void cleanUp() {
        chatEvent.unsubscribe();
        chatEvent = null;
    }
}
