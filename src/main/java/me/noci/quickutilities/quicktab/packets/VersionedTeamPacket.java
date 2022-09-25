package me.noci.quickutilities.quicktab.packets;

import lombok.Getter;
import me.noci.quickutilities.quicktab.builder.TabListTeam;

public interface VersionedTeamPacket {

    Object removeTeamPacket(TabListTeam team);

    Object createTeamPacket(TabListTeam team);

    String protocolVersion();

    default String protocolInfo() {
        return protocolVersion() + " (" + getClass().getSimpleName() + ")";
    }

    enum TeamMode {
        CREATE(0),
        REMOVE(1),
        UPDATE(2),
        ADD_PLAYERS(3),
        REMOVE_PLAYERS(4);

        @Getter private final int mode;

        TeamMode(int modeID) {
            this.mode = modeID;
        }

    }

}
