package me.noci.quickutilities.input.sign;

import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.cryptomorin.xseries.ReflectionUtils;
import me.noci.quickutilities.input.BasePlayerInput;
import me.noci.quickutilities.input.functions.InputExecutor;
import me.noci.quickutilities.input.sign.packets.SignPacketHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;

public class PlayerSignInput extends BasePlayerInput implements Listener {

    private static final int MIN_SIGN_HEIGHT_NETHER = 5;
    private static final int MIN_SIGN_HEIGHT_END = 1;
    private static final int MIN_SIGN_HEIGHT_WORLD = ReflectionUtils.v(18, -59).orElse(6);
    private static final int SIGN_Y_DISTANCE = 5;

    private final SignPacketHandler handler;
    private final WrappedBlockData oldData;
    private final BlockPosition position;

    protected PlayerSignInput(SignPacketHandler handler, Player player, Material signType, String textColor, byte glowingText, int inputLine, List<String> signLines, InputExecutor inputExecutor) {
        super(player, inputExecutor);
        this.handler = handler;
        SignData signData = new SignData(textColor, signType, signLines, inputLine, glowingText);

        Location location = player.getLocation();
        position = new BlockPosition(location.getBlockX(), getSignY(location), location.getBlockZ());
        Block oldBlock = location.getBlock();
        oldData = WrappedBlockData.createData(oldBlock.getType(), oldBlock.getData());

        handler.openEditor(player, position, signData);
        handler.packetListener(position, signData, input -> {
            if (inputExecutor == null || input.isEmpty() || input.isBlank()) {
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

    private static int getSignY(Location location) {
        int minHeight = switch (location.getWorld().getEnvironment()) {
            case NORMAL, CUSTOM -> MIN_SIGN_HEIGHT_WORLD;
            case NETHER -> MIN_SIGN_HEIGHT_NETHER;
            case THE_END -> MIN_SIGN_HEIGHT_END;
        };

        int y = location.getBlockY();
        if ((y - SIGN_Y_DISTANCE) < minHeight) y += SIGN_Y_DISTANCE;
        else y -= SIGN_Y_DISTANCE;

        return y;
    }

}
