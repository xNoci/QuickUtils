package me.noci.quickutilities.input.sign.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.noci.quickutilities.QuickUtils;
import me.noci.quickutilities.input.sign.SignData;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

public class SignPacketHandlerV1_8 implements SignPacketHandler {

    @Override
    @SuppressWarnings("deprecation")
    public void openEditor(Player player, BlockPosition position, SignData signData) {
        var protocol = ProtocolLibrary.getProtocolManager();

        PacketContainer blockChange = protocol.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
        blockChange.getBlockPositionModifier().write(0, position);
        blockChange.getBlockData().write(0, WrappedBlockData.createData(signData.signType()));

        PacketContainer updateSign = protocol.createPacket(PacketType.Play.Server.UPDATE_SIGN);
        updateSign.getBlockPositionModifier().write(0, position);
        updateSign.getChatComponentArrays().write(0, signText(signData));

        PacketContainer openSign = protocol.createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
        openSign.getBlockPositionModifier().write(0, position);

        protocol.sendServerPacket(player, blockChange);
        protocol.sendServerPacket(player, updateSign);
        protocol.sendServerPacket(player, openSign);
    }

    @Override
    public void revertBlock(Player player, BlockPosition position, WrappedBlockData oldData) {
        var protocol = ProtocolLibrary.getProtocolManager();

        PacketContainer revertBlockChange = protocol.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
        revertBlockChange.getBlockPositionModifier().write(0, position);
        revertBlockChange.getBlockData().write(0, oldData);

        protocol.sendServerPacket(player, revertBlockChange);
    }

    @Override
    public void packetListener(BlockPosition position, SignData signData, Consumer<String> signInput) {
        var protocol = ProtocolLibrary.getProtocolManager();

        protocol.addPacketListener(new PacketAdapter(QuickUtils.instance(), ListenerPriority.NORMAL, PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer container = event.getPacket();
                if (!container.getBlockPositionModifier().read(0).equals(position)) return;
                try {
                    event.setCancelled(true);
                    String input = container.getChatComponentArrays().read(0)[signData.inputLine()].getJson();
                    input = input.substring(1, input.length() - 1);
                    signInput.accept(input);

                    protocol.removePacketListener(this);
                } catch (Exception e) {
                    //NO NEED TO HANDLE
                }
            }
        });
    }

    @Override
    public int version() {
        return 8;
    }

    private WrappedChatComponent[] signText(SignData signData) {
        List<String> lines = signData.signLines();

        WrappedChatComponent[] components = new WrappedChatComponent[4];
        int i = 0;
        for (int lineIndex = 0; lineIndex < 4; lineIndex++) {
            if (lineIndex == signData.inputLine() || i >= lines.size()) {
                components[lineIndex] = WrappedChatComponent.fromLegacyText("");
                continue;
            }

            components[lineIndex] = WrappedChatComponent.fromLegacyText(lines.get(i));
            i++;
        }

        return components;
    }

}
