package me.noci.quickutilities.input;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class BasePlayerInput {

    protected Player player;
    protected Consumer<String> playerInput;
    @Getter protected boolean inputMode = true;
    private Consumer<Player> onCancel;

    public BasePlayerInput(Player player, Consumer<String> playerInput) {
        this.player = player;
        this.playerInput = playerInput;
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
        this.playerInput = null;
    }

    public abstract void cleanUp();

}
