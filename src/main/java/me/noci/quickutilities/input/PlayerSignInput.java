package me.noci.quickutilities.input;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.comphenix.protocol.wrappers.WrappedRegistrable;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.Lists;
import me.noci.quickutilities.QuickUtils;
import me.noci.quickutilities.input.functions.InputExecutor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class PlayerSignInput extends BasePlayerInput implements Listener {

    private static final String LINE_FORMAT = "{\"text\": \"%s\"}";
    private static final String TEXT_COLOR = "black";
    private static final boolean HAS_GLOWING_TEXT = false;

    private final BlockPosition position;
    private final WrappedBlockData oldData;
    private final List<String> signLines;
    private final int inputLine;

    protected PlayerSignInput(Player player, int inputLine, List<String> signLines, InputExecutor inputExecutor) {
        super(player, inputExecutor);
        this.signLines = signLines;
        this.inputLine = Math.max(1, Math.min(inputLine, 4)) - 1;

        Location location = player.getLocation();
        position = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        Block oldBlock = location.getBlock();
        oldData = WrappedBlockData.createData(oldBlock.getType(), oldBlock.getData());

        openEditor();
        listenerUpdate();
    }

    @Override
    public void cleanUp() {
        revertBlock();
    }

    private void openEditor() {
        var protocol = ProtocolLibrary.getProtocolManager();

        PacketContainer blockChange = protocol.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
        blockChange.getBlockPositionModifier().write(0, position);
        blockChange.getBlockData().write(0, WrappedBlockData.createData(XMaterial.OAK_WALL_SIGN.parseMaterial()));

        PacketContainer blockData = protocol.createPacket(PacketType.Play.Server.TILE_ENTITY_DATA);
        blockData.getBlockPositionModifier().write(0, position);
        blockData.getBlockEntityTypeModifier().write(0, WrappedRegistrable.blockEntityType("sign"));
        blockData.getNbtModifier().write(0, signCompound());

        PacketContainer openSign = protocol.createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
        openSign.getBlockPositionModifier().write(0, position);
        openSign.getBooleans().write(0, true);

        protocol.sendServerPacket(player, blockChange);
        protocol.sendServerPacket(player, blockData);
        protocol.sendServerPacket(player, openSign);
    }

    private void revertBlock() {
        var protocol = ProtocolLibrary.getProtocolManager();

        PacketContainer revertBlockChange = protocol.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
        revertBlockChange.getBlockPositionModifier().write(0, position);
        revertBlockChange.getBlockData().write(0, oldData);
        protocol.sendServerPacket(player, revertBlockChange);
    }

    private void listenerUpdate() {
        var protocol = ProtocolLibrary.getProtocolManager();

        protocol.addPacketListener(new PacketAdapter(QuickUtils.instance(), ListenerPriority.NORMAL, PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer container = event.getPacket();
                if (!container.getBlockPositionModifier().read(0).equals(position)) return;
                try {
                    event.setCancelled(true);

                    String input = container.getStringArrays().read(0)[inputLine];
                    if (inputExecutor == null || input.isEmpty()) {
                        stopInput(true);
                        return;
                    }
                    inputExecutor.execute(input);
                    stopInput(false);
                } catch (Exception e) {
                    //NO NEED TO HANDLE
                }
            }
        });
    }

    private NbtCompound signCompound() {
        List<NbtBase<?>> tags = Lists.newArrayList();

        tags.add(NbtFactory.of("is_waxed", (byte) 0));
        tags.add(textCompound("front_text", signLines));
        tags.add(textCompound("back_text", List.of()));

        return NbtFactory.ofCompound("", tags);
    }

    private NbtCompound textCompound(String compoundName, List<String> lines) {
        List<NbtBase<?>> tags = Lists.newArrayList();
        List<String> text = Lists.newArrayList();
        int i = 0;
        for (int lineIndex = 0; lineIndex < 4; lineIndex++) {
            if (lineIndex == inputLine || i >= lines.size()) {
                text.add(String.format(LINE_FORMAT, ""));
                continue;
            }

            text.add(String.format(LINE_FORMAT, lines.get(i)));
            i++;
        }
        tags.add(NbtFactory.of("has_glowing_text", (byte) (HAS_GLOWING_TEXT ? 1 : 0)));
        tags.add(NbtFactory.of("color", TEXT_COLOR));
        tags.add(NbtFactory.ofList("messages", text));
        return NbtFactory.ofCompound(compoundName, tags);
    }

    @EventHandler
    private void handlePlayerQuit(PlayerQuitEvent event) {
        stopInput(true);
    }

}
