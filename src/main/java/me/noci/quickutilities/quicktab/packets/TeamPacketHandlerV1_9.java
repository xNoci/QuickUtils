package me.noci.quickutilities.quicktab.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import me.noci.quickutilities.quicktab.builder.TabListTeam;

import java.util.Collection;

public class TeamPacketHandlerV1_9 implements TeamPacketHandler {

    @Override
    public Object removeTeamPacket(TabListTeam team) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);

        packet.getIntegers().write(1, TeamMode.REMOVE.getMode());
        packet.getStrings().write(0, team.getTeamName());

        return packet.getHandle();
    }

    @Override
    public Object createTeamPacket(TabListTeam team) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);

        packet.getIntegers().write(1, TeamMode.CREATE.getMode());
        packet.getStrings().write(0, team.getTeamName());

        packet.getStrings().write(1, team.getDisplayName());
        packet.getStrings().write(2, team.getPrefix());
        packet.getStrings().write(3, team.getSuffix());
        packet.getStrings().write(4, team.getNameTagVisibility().getId());
        packet.getStrings().write(5, team.getCollisionRule().getId());

        //This should work but maybe create mapping function
        packet.getIntegers().write(0, team.getColor().ordinal());
        packet.getIntegers().write(2, team.getPackOptionData());

        packet.getSpecificModifier(Collection.class).write(0, team.getEntries());

        return packet.getHandle();
    }

    @Override
    public String protocolVersion() {
        return "v1.9";
    }

    @Override
    public int[] supportedVersions() {
        return new int[]{9, 10, 11, 12};
    }

}
