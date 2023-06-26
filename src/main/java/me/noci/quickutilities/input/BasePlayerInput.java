package me.noci.quickutilities.input;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public abstract class BasePlayerInput {

    protected Player player;
    protected InputExecutor inputExecutor;
    @Getter protected boolean inputMode = true;
    private Consumer<Player> onCancel;

    public BasePlayerInput(Player player, InputExecutor inputExecutor) {
        this.player = player;
        this.inputExecutor = inputExecutor;
    }

    public void onCancel(Consumer<Player> task) {
        this.onCancel = task;
    }

    protected void stopInput(boolean canceled) {
        cleanUp();
        if (onCancel != null && player.isOnline() && canceled) {
            onCancel.accept(player);
        }

        this.inputMode = false;
        this.player = null;
        this.inputExecutor = null;
    }

    public abstract void cleanUp();

    @FunctionalInterface
    public interface InputExecutor {
        void execute(String input);
    }

}
