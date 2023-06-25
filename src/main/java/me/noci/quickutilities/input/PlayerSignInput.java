package me.noci.quickutilities.input;

import me.noci.quickutilities.utils.ProtocolLibHook;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class PlayerSignInput extends AbstractPlayerInput {

    private PlayerSignInput(JavaPlugin plugin, Player player, Consumer<String> playerInput) {
        super(plugin, player, playerInput);
        //TODO SUPPORT
        if (true) throw new UnsupportedOperationException("CURRENTLY NOT SUPPORTED");
        if (!ProtocolLibHook.isEnabled()) {
            throw new UnsupportedOperationException("Only available while using ProtocolLib.");
        }
    }

}
