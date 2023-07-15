package me.noci.quickutilities.input.sign;

import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import me.noci.quickutilities.input.BasePlayerInput;
import me.noci.quickutilities.input.functions.InputExecutor;
import me.noci.quickutilities.input.sign.packets.SignPacketHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class PlayerSignInput extends BasePlayerInput implements Listener {

    private final SignPacketHandler versionedSignPacket;

    private final BlockPosition position;
    private final SignData signData;
    private final WrappedBlockData oldData;

    protected PlayerSignInput(SignPacketHandler signPacket, Player player, Material signType, String textColor, byte glowingText, int inputLine, List<String> signLines, InputExecutor inputExecutor) {
        super(player, inputExecutor);
        this.versionedSignPacket = signPacket;
        this.signData = new SignData(textColor, signType, signLines, inputLine, glowingText);

        Location location = player.getLocation();
        position = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        Block oldBlock = location.getBlock();
        oldData = WrappedBlockData.createData(oldBlock.getType(), oldBlock.getData());

        versionedSignPacket.openEditor(player, position, signData);
        versionedSignPacket.packetListener(position, signData, input -> {
            if (inputExecutor == null || input.isEmpty()) {
                stopInput(true);
                return;
            }
            inputExecutor.execute(input);
            stopInput(false);
        });
    }

    @Override
    public void cleanUp() {
        versionedSignPacket.revertBlock(player, position, oldData);
    }


    @EventHandler
    private void handlePlayerQuit(PlayerQuitEvent event) {
        stopInput(true);
    }

}
