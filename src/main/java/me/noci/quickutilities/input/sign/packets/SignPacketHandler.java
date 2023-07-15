package me.noci.quickutilities.input.sign.packets;

import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import me.noci.quickutilities.input.sign.SignData;
import me.noci.quickutilities.quicktab.utils.packets.PacketHandler;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface SignPacketHandler extends PacketHandler<SignPacketHandler> {

    String JSON_TEXT_FORMAT = "{\"text\": \"%s\"}";

    static String jsonText(String text) {
        return JSON_TEXT_FORMAT.formatted(text);
    }

    void openEditor(Player player, BlockPosition position, SignData signData);

    void revertBlock(Player player, BlockPosition position, WrappedBlockData oldData);

    void packetListener(BlockPosition position, SignData signData, Consumer<String> signInput);

}
