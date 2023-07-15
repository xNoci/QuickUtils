package me.noci.quickutilities.input.sign.packets;

import com.google.common.collect.Sets;
import me.noci.quickutilities.quicktab.utils.packets.PacketHandleInfo;
import me.noci.quickutilities.quicktab.utils.packets.PacketHandlerManager;

import java.util.Set;

public class SignPacketHandlerManager implements PacketHandlerManager<SignPacketHandler> {

    private final SignPacketHandler handle;

    public SignPacketHandlerManager() {
        PacketHandleInfo<SignPacketHandler> unknownVersion = PacketHandleInfo.unknown(new SignPacketHandlerUnknown());

        Set<PacketHandleInfo<SignPacketHandler>> handleInfos = Sets.newHashSet();
        try {
            handleInfos.add(PacketHandleInfo.version(new SignPacketHandlerV1_20(), 20));
        } catch (NoClassDefFoundError ignore) {
        }

        handle = findSupportedHandle(handleInfos, unknownVersion);
    }

    @Override
    public boolean requiresProtocolLib() {
        return true;
    }

    @Override
    public SignPacketHandler handler() {
        return handle;
    }

    @Override
    public Class<SignPacketHandler> getHandlerType() {
        return SignPacketHandler.class;
    }
}
