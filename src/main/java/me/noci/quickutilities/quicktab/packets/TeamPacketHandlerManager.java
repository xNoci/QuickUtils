package me.noci.quickutilities.quicktab.packets;

import com.google.common.collect.Sets;
import me.noci.quickutilities.packethandler.PacketHandlerInfo;
import me.noci.quickutilities.packethandler.PacketHandlerManager;

import java.util.Set;

public class TeamPacketHandlerManager implements PacketHandlerManager<TeamPacketHandler> {

    private final PacketHandlerInfo<TeamPacketHandler> handlerInfo;

    public TeamPacketHandlerManager() {
        PacketHandlerInfo<TeamPacketHandler> unknownVersion = PacketHandlerInfo.version(new TeamPacketHandlerUnknown(), -1);

        Set<PacketHandlerInfo<TeamPacketHandler>> handlerInfos = Sets.newHashSet();
        handlerInfos.add(PacketHandlerInfo.version(new TeamPacketHandlerV1_8(), 8));
        handlerInfos.add(PacketHandlerInfo.version(new TeamPacketHandlerV1_9(), 9, 10, 11, 12));
        handlerInfos.add(PacketHandlerInfo.version(new TeamPacketHandlerV1_13(), 13, 14, 15, 16));
        handlerInfos.add(PacketHandlerInfo.version(new TeamPacketHandlerV1_17(), 17, 18, 19, 20));

        handlerInfo = findSupportedHandler(handlerInfos, unknownVersion);
    }

    @Override
    public boolean requiresProtocolLib() {
        return true;
    }

    @Override
    public PacketHandlerInfo<TeamPacketHandler> getHandlerInfo() {
        return handlerInfo;
    }

    @Override
    public Class<TeamPacketHandler> getHandlerType() {
        return TeamPacketHandler.class;
    }

}
