package me.noci.quickutilities.quicktab.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedTeamParameters;
import me.noci.quickutilities.quicktab.builder.TeamInfo;

import java.util.Collection;
import java.util.Optional;

public class TeamPacketHandlerV1_20_4 implements TeamPacketHandler {

    @Override
    public Object removeTeamPacket(TeamInfo team) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);

        packet.getIntegers().write(0, TeamMode.REMOVE.getMode());
        packet.getStrings().write(0, team.teamName());

        return packet.getHandle();
    }

    @Override
    public Object createTeamPacket(TeamInfo team) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);

        packet.getIntegers().write(0, TeamMode.CREATE.getMode());
        packet.getStrings().write(0, team.teamName());
        packet.getSpecificModifier(Collection.class).write(0, team.entries());

        WrappedTeamParameters teamParameters = WrappedTeamParameters.newBuilder()
                .displayName(WrappedChatComponent.fromLegacyText(team.displayName()))
                .prefix(WrappedChatComponent.fromLegacyText(team.prefix()))
                .suffix(WrappedChatComponent.fromLegacyText(team.suffix()))
                .nametagVisibility(team.nameTagVisibility().getId())
                .collisionRule(team.collisionRule().getId())
                .options(team.packOptionData())
                .color(EnumWrappers.ChatFormatting.fromBukkit(team.color()))
                .build();

        packet.getOptionalTeamParameters().write(0, Optional.of(teamParameters));


        return packet.getHandle();
    }

    @Override
    public int version() {
        return 20;
    }

    @Override
    public int patch() {
        return 4;
    }
}
