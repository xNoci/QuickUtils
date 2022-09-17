package me.noci.quickutilities.quicktab.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import me.noci.quickutilities.quicktab.builder.TabListTeam;

import java.util.Collection;

public class TabListTeamPacketV1_8 implements TabListTeamPacket {

    @Override
    public Object removeTeamPacket(TabListTeam team) {
        PacketContainer scoreboardTeam = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);

        scoreboardTeam.getStrings().write(0, team.getTeamName());
        scoreboardTeam.getIntegers().write(1, TeamMode.REMOVE.getMode());

        return scoreboardTeam.getHandle();
    }

    @Override
    public Object createTeamPacket(TabListTeam team) {
        PacketContainer scoreboardTeam = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);
        scoreboardTeam.getStrings().write(0, team.getTeamName());
        scoreboardTeam.getStrings().write(1, team.getDisplayName());
        scoreboardTeam.getStrings().write(2, team.getPrefix());
        scoreboardTeam.getStrings().write(3, team.getSuffix());
        scoreboardTeam.getStrings().write(4, team.getNameTagVisibility().getId());

        //This should work but maybe create mapping function
        scoreboardTeam.getIntegers().write(0, team.getColor().ordinal());
        scoreboardTeam.getIntegers().write(1, TeamMode.CREATE.getMode());
        scoreboardTeam.getIntegers().write(2, team.getPackOptionData());

        scoreboardTeam.getSpecificModifier(Collection.class).write(0, team.getEntries());

        return scoreboardTeam.getHandle();
    }
}
