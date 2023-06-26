package me.noci.quickutilities.input;

import lombok.Getter;
import org.bukkit.entity.Player;

public abstract class BasePlayerInput {

    protected Player player;
    protected InputExecutor inputExecutor;
    private CanceledInput canceledInput;
    @Getter protected boolean inputMode = true;

    public BasePlayerInput(Player player, InputExecutor inputExecutor) {
        this.player = player;
        this.inputExecutor = inputExecutor;
    }

    public void onCancel(CanceledInput task) {
        this.canceledInput = task;
    }

    protected void stopInput(boolean canceled) {
        cleanUp();
        if (canceledInput != null && player.isOnline() && canceled) {
            canceledInput.execute(player);
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

    @FunctionalInterface
    public interface CanceledInput {
        void execute(Player player);
    }

}
