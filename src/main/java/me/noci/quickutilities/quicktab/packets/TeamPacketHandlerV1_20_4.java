package me.noci.quickutilities.quicktab.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedTeamParameters;
import me.noci.quickutilities.quicktab.builder.TeamInfo;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;

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

        String displayName = JSONComponentSerializer.json().serialize(team.displayName());
        String prefix = JSONComponentSerializer.json().serialize(team.prefix());
        String suffix = JSONComponentSerializer.json().serialize(team.suffix());

        WrappedTeamParameters teamParameters = WrappedTeamParameters.newBuilder()
                .displayName(WrappedChatComponent.fromJson(displayName))
                .prefix(WrappedChatComponent.fromJson(prefix))
                .suffix(WrappedChatComponent.fromJson(suffix))
                .nametagVisibility(team.nameTagVisibility().getId())
                .collisionRule(team.collisionRule().getId())
                .options(team.packOptionData())
                .color(team.color())
                .build();

        packet.getOptionalTeamParameters().write(0, Optional.of(teamParameters));

        return packet.getHandle();
    }

    @Override
    public int major() {
        return 1;
    }

    @Override
    public int minor() {
        return 20;
    }

    @Override
    public int patch() {
        return 4;
    }
}
