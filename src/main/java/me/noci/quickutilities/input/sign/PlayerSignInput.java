package me.noci.quickutilities.input.sign;

import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import me.noci.quickutilities.input.BasePlayerInput;
import me.noci.quickutilities.input.functions.InputExecutor;
import me.noci.quickutilities.input.sign.packets.SignPacketHandler;
import me.noci.quickutilities.utils.MathUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class PlayerSignInput extends BasePlayerInput implements Listener {

    private final SignPacketHandler handler;

    private final BlockPosition position;
    private final WrappedBlockData oldData;

    protected PlayerSignInput(SignPacketHandler handler, Player player, Material signType, String textColor, byte glowingText, int inputLine, List<String> signLines, InputExecutor inputExecutor) {
        super(player, inputExecutor);
        this.handler = handler;
        SignData signData = new SignData(textColor, signType, signLines, inputLine, glowingText);

        Location location = player.getLocation();
        int y = (location.getBlockY() + 100) % location.getWorld().getMaxHeight(); //TODO TEST
        y = MathUtils.clamp(20, location.getWorld().getMaxHeight(), y);
        position = new BlockPosition(location.getBlockX(), y, location.getBlockZ());
        Block oldBlock = location.getBlock();
        oldData = WrappedBlockData.createData(oldBlock.getType(), oldBlock.getData());

        handler.openEditor(player, position, signData);
        handler.packetListener(position, signData, input -> {
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
        handler.revertBlock(player, position, oldData);
    }


    @EventHandler
    private void handlePlayerQuit(PlayerQuitEvent event) {
        stopInput(true);
    }

}
