package me.noci.quickutilities.input;

import lombok.Getter;
import me.noci.quickutilities.events.Events;
import me.noci.quickutilities.events.subscriber.SubscribedEvent;
import me.noci.quickutilities.input.functions.CanceledInput;
import me.noci.quickutilities.input.functions.InputExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

public abstract class BasePlayerInput implements Input {

    protected Player player;
    protected InputExecutor inputExecutor;
    private CanceledInput canceledInput;
    @Getter protected boolean inputMode = true;
    private SubscribedEvent<PlayerQuitEvent> quitEvent;

    protected BasePlayerInput(Player player, InputExecutor inputExecutor) {
        this.player = player;
        this.inputExecutor = inputExecutor;
        quitEvent = Events.subscribe(PlayerQuitEvent.class)
                .expireAfter(1)
                .filter(event -> !event.getPlayer().getUniqueId().equals(player.getUniqueId()))
                .handle(event -> {
                    stopInput(true);
                });
    }

    @Override
    public void onCancel(CanceledInput task) {
        this.canceledInput = task;
    }

    protected void stopInput(boolean canceled) {
        cleanUp();
        quitEvent.unsubscribe();
        quitEvent = null;
        if (canceledInput != null && player.isOnline() && canceled) {
            canceledInput.execute(player);
        }

        this.inputMode = false;
        this.player = null;
        this.inputExecutor = null;
    }

    public abstract void cleanUp();
}
