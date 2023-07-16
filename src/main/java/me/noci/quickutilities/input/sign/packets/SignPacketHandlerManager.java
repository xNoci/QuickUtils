package me.noci.quickutilities.input.sign.packets;

import com.google.common.collect.Sets;
import me.noci.quickutilities.packethandler.PacketHandlerInfo;
import me.noci.quickutilities.packethandler.PacketHandlerManager;

import java.util.Set;

public class SignPacketHandlerManager implements PacketHandlerManager<SignPacketHandler> {

    private final PacketHandlerInfo<SignPacketHandler> handlerInfo;

    public SignPacketHandlerManager() {
        Set<PacketHandlerInfo<SignPacketHandler>> handlerInfos = Sets.newHashSet();
        try {
            handlerInfos.add(PacketHandlerInfo.version(new SignPacketHandlerV1_20(), 20));
        } catch (NoClassDefFoundError ignore) {
        }

        handlerInfo = findSupportedHandler(handlerInfos);
    }

    @Override
    public boolean requiresProtocolLib() {
        return true;
    }

    @Override
    public PacketHandlerInfo<SignPacketHandler> getHandlerInfo() {
        return handlerInfo;
    }

    @Override
    public Class<SignPacketHandler> getHandlerType() {
        return SignPacketHandler.class;
    }

}
