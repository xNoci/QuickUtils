package me.noci.quickutilities.quicktab.packets;

import lombok.Getter;
import me.noci.quickutilities.packethandler.PacketHandler;
import me.noci.quickutilities.quicktab.builder.TabListTeam;

public interface TeamPacketHandler extends PacketHandler<TeamPacketHandler> {

    Object removeTeamPacket(TabListTeam team);

    Object createTeamPacket(TabListTeam team);

    @Getter
    enum TeamMode {
        CREATE(0),
        REMOVE(1),
        UPDATE(2),
        ADD_PLAYERS(3),
        REMOVE_PLAYERS(4);

        private final int mode;

        TeamMode(int modeID) {
            this.mode = modeID;
        }

    }

}
