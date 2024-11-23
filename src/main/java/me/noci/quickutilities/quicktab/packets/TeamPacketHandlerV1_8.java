package me.noci.quickutilities.quicktab.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import me.noci.quickutilities.quicktab.builder.TeamInfo;

import java.util.Collection;

public class TeamPacketHandlerV1_8 implements TeamPacketHandler {

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

        packet.getStrings().write(1, team.displayName());
        packet.getStrings().write(2, team.prefix());
        packet.getStrings().write(3, team.suffix());
        packet.getStrings().write(4, team.nameTagVisibility().getId());

        //This should work but maybe create mapping function
        packet.getIntegers().write(0, team.color().ordinal());
        packet.getIntegers().write(2, team.packOptionData());

        packet.getSpecificModifier(Collection.class).write(0, team.entries());

        return packet.getHandle();
    }

    @Override
    public int version() {
        return 8;
    }

}
