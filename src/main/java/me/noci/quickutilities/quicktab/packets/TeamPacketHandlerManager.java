package me.noci.quickutilities.quicktab.packets;

import me.noci.quickutilities.packethandler.PacketHandlerManager;

public class TeamPacketHandlerManager extends PacketHandlerManager<TeamPacketHandler> {

    public TeamPacketHandlerManager() {
        super(true, new TeamPacketHandlerV1_8(), new TeamPacketHandlerV1_9(), new TeamPacketHandlerV1_13(), new TeamPacketHandlerV1_17());
    }

}
