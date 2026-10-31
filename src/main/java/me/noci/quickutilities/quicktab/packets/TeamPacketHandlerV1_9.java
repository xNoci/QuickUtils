package me.noci.quickutilities.quicktab.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import me.noci.quickutilities.quicktab.builder.TeamInfo;
import me.noci.quickutilities.utils.Legacy;

import java.util.Collection;

public class TeamPacketHandlerV1_9 implements TeamPacketHandler {

    @Override
    public Object removeTeamPacket(TeamInfo team) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);

        packet.getIntegers().write(1, TeamMode.REMOVE.getMode());
        packet.getStrings().write(0, team.teamName());

        return packet.getHandle();
    }

    @Override
    public Object createTeamPacket(TeamInfo team) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);

        packet.getIntegers().write(1, TeamMode.CREATE.getMode());
        packet.getStrings().write(0, team.teamName());

        packet.getStrings().write(1, Legacy.serialize(team.displayName()).substring(0, 16));
        packet.getStrings().write(2, Legacy.serialize(team.prefix()).substring(0, 16));
        packet.getStrings().write(3, Legacy.serialize(team.prefix()).substring(0, 16));
        packet.getStrings().write(4, team.nameTagVisibility().getId());
        packet.getStrings().write(5, team.collisionRule().getId());

        //This should work but maybe create mapping function
        packet.getIntegers().write(0, team.color().ordinal());
        packet.getIntegers().write(2, team.packOptionData());

        packet.getSpecificModifier(Collection.class).write(0, team.entries());

        return packet.getHandle();
    }

    @Override
    public int major() {
        return 1;
    }

    @Override
    public int minor() {
        return 9;
    }
}
