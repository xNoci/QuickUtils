package me.noci.quickutilities.input.sign.packets;

import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.cryptomorin.xseries.ReflectionUtils;
import me.noci.quickutilities.input.sign.SignData;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class SignPacketHandlerUnknown implements SignPacketHandler {

    @Override
    public void openEditor(Player player, BlockPosition position, SignData signData) {
        throw new UnsupportedOperationException("Your server version is currently not supported. Your are on version %s.".formatted(ReflectionUtils.MINOR_NUMBER));
    }

    @Override
    public void revertBlock(Player player, BlockPosition position, WrappedBlockData oldData) {
        throw new UnsupportedOperationException("Your server version is currently not supported. Your are on version %s.".formatted(ReflectionUtils.MINOR_NUMBER));
    }

    @Override
    public void packetListener(BlockPosition position, SignData signData, Consumer<String> signInput) {
        throw new UnsupportedOperationException("Your server version is currently not supported. Your are on version %s.".formatted(ReflectionUtils.MINOR_NUMBER));
    }

    @Override
    public String protocolVersion() {
        return "Unknown";
    }
}
