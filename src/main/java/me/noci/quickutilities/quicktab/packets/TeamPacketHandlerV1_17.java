package me.noci.quickutilities.quicktab.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.noci.quickutilities.quicktab.builder.TeamInfo;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;

import java.util.Collection;
import java.util.Optional;

public class TeamPacketHandlerV1_17 implements TeamPacketHandler {

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

        //https://www.spigotmc.org/threads/help-with-protocollib-packet-formating.528259/
        Optional<InternalStructure> structureOptional = packet.getOptionalStructures().read(0);
        if (structureOptional.isPresent()) {
            InternalStructure structure = structureOptional.get();

            String displayName = JSONComponentSerializer.json().serialize(team.displayName());
            String prefix = JSONComponentSerializer.json().serialize(team.prefix());
            String suffix = JSONComponentSerializer.json().serialize(team.suffix());

            structure.getChatComponents().write(0, WrappedChatComponent.fromJson(displayName));
            structure.getChatComponents().write(1, WrappedChatComponent.fromJson(prefix));
            structure.getChatComponents().write(2, WrappedChatComponent.fromJson(suffix));

            structure.getStrings().write(0, team.nameTagVisibility().getId());
            structure.getStrings().write(1, team.collisionRule().getId());

            structure.getModifier().withType(EnumWrappers.getChatFormattingClass())
                    .write(0, EnumWrappers.getChatFormattingConverter().getGeneric(team.color()));

            structure.getIntegers().write(0, team.packOptionData());

            packet.getOptionalStructures().write(0, Optional.of(structure));
        }

        return packet.getHandle();
    }

    @Override
    public int major() {
        return 1;
    }

    @Override
    public int minor() {
        return 17;
    }
}
