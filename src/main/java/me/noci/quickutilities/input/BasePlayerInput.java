package me.noci.quickutilities.input;

import lombok.Getter;
import me.noci.quickutilities.input.functions.CanceledInput;
import me.noci.quickutilities.input.functions.InputExecutor;
import org.bukkit.entity.Player;

public abstract class BasePlayerInput implements Input {

    protected Player player;
    protected InputExecutor inputExecutor;
    private CanceledInput canceledInput;
    @Getter protected boolean inputMode = true;

    protected BasePlayerInput(Player player, InputExecutor inputExecutor) {
        this.player = player;
        this.inputExecutor = inputExecutor;
    }

    @Override
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
}
