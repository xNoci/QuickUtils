package me.noci.quickutilities.input;

import me.noci.quickutilities.utils.ProtocolLibHook;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class PlayerSignInput extends BasePlayerInput {

    private PlayerSignInput(Player player, Consumer<String> playerInput) {
        super(player, playerInput);
        //TODO SUPPORT
        if (true) throw new UnsupportedOperationException("CURRENTLY NOT SUPPORTED");
        if (!ProtocolLibHook.isEnabled()) {
            throw new UnsupportedOperationException("Only available while using ProtocolLib.");
        }
    }

    @Override
    public void cleanUp() {

    }

}
