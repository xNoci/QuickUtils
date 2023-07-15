package me.noci.quickutilities.quicktab.packets;

import com.google.common.collect.Sets;
import me.noci.quickutilities.quicktab.utils.packets.PacketHandleInfo;
import me.noci.quickutilities.quicktab.utils.packets.PacketHandlerManager;

import java.util.Set;

public class TeamPacketHandlerManager implements PacketHandlerManager<TeamPacketHandler> {

    private final TeamPacketHandler handle;

    public TeamPacketHandlerManager() {
        PacketHandleInfo<TeamPacketHandler> unknownVersion = PacketHandleInfo.version(new TeamPacketHandlerUnknown(), -1);

        Set<PacketHandleInfo<TeamPacketHandler>> handleInfos = Sets.newHashSet();
        handleInfos.add(PacketHandleInfo.version(new TeamPacketHandlerV1_8(), 8));
        handleInfos.add(PacketHandleInfo.version(new TeamPacketHandlerV1_9(), 9, 10, 11, 12));
        handleInfos.add(PacketHandleInfo.version(new TeamPacketHandlerV1_13(), 13, 14, 15, 16));
        handleInfos.add(PacketHandleInfo.version(new TeamPacketHandlerV1_17(), 17, 18, 19, 20));

        handle = findSupportedHandle(handleInfos, unknownVersion);
    }

    @Override
    public boolean requiresProtocolLib() {
        return true;
    }

    @Override
    public TeamPacketHandler handler() {
        return handle;
    }

    @Override
    public Class<TeamPacketHandler> getHandlerType() {
        return TeamPacketHandler.class;
    }
}
