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
    private BiConsumer<Player, Boolean> onCancel;

    public BasePlayerInput(Player player, Consumer<String> playerInput) {
        this.player = player;
        this.playerInput = playerInput;
    }

    public void onCancel(BiConsumer<Player, Boolean> task) {
        this.onCancel = task;
    }

    protected void cancel(boolean forced) {
        cleanUp();
        if (onCancel != null && player.isOnline()) {
            onCancel.accept(player, forced);
        }

        this.inputMode = false;
        this.player = null;
        this.playerInput = null;
    }

    public abstract void cleanUp();

}
