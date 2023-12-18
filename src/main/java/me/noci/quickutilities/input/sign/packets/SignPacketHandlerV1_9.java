package me.noci.quickutilities.input.sign.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.google.common.collect.Lists;
import me.noci.quickutilities.QuickUtils;
import me.noci.quickutilities.input.sign.SignData;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;

public class SignPacketHandlerV1_9 implements SignPacketHandler {

    @Override
    public void openEditor(Player player, BlockPosition position, SignData signData) {
        var protocol = ProtocolLibrary.getProtocolManager();

        PacketContainer blockChange = protocol.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
        blockChange.getBlockPositionModifier().write(0, position);
        blockChange.getBlockData().write(0, WrappedBlockData.createData(signData.signType()));

        PacketContainer tileEntityData = protocol.createPacket(PacketType.Play.Server.TILE_ENTITY_DATA);
        tileEntityData.getBlockPositionModifier().write(0, position);
        tileEntityData.getIntegers().write(0, 9);
        tileEntityData.getNbtModifier().write(0, signText(signData));


        PacketContainer openSign = protocol.createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
        openSign.getBlockPositionModifier().write(0, position);

        protocol.sendServerPacket(player, blockChange);
        protocol.sendServerPacket(player, tileEntityData);
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
                    String input = container.getStringArrays().read(0)[signData.inputLine()];
                    signInput.accept(input);

                    protocol.removePacketListener(this);
                } catch (Exception e) {
                    //NO NEED TO HANDLE
                }
            }
        });
    }

    @Override
    public String protocolVersion() {
        return "v1.9";
    }

    @Override
    public int[] supportedVersions() {
        return new int[]{9, 10, 11, 12, 13, 14, 15, 16, 17};
    }

    private NbtCompound signText(SignData signData) {
        List<NbtBase<?>> tags = Lists.newArrayList();

        List<String> lines = signData.signLines();
        int i = 0;
        for (int lineIndex = 0; lineIndex < 4; lineIndex++) {
            if (lineIndex == signData.inputLine() || i >= lines.size()) {
                tags.add(NbtFactory.of("Text" + (lineIndex + 1), SignPacketHandler.jsonText("")));
                continue;
            }
            tags.add(NbtFactory.of("Text" + (lineIndex + 1), SignPacketHandler.jsonText(lines.get(i))));
            i++;
        }

        tags.add(NbtFactory.of("Color", signData.textColor()));
        tags.add(NbtFactory.of("GlowingText", signData.glowingText()));

        return NbtFactory.ofCompound("", tags);
    }

}
