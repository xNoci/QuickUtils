package me.noci.quickutilities.quicktab.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.noci.quickutilities.quicktab.builder.TabListTeam;
import org.bukkit.ChatColor;

import java.util.Collection;
import java.util.Optional;

public class TeamPacketHandlerV1_17 implements TeamPacketHandler {

    @Override
    public Object removeTeamPacket(TabListTeam team) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);

        packet.getIntegers().write(0, TeamMode.REMOVE.getMode());
        packet.getStrings().write(0, team.getTeamName());

        return packet.getHandle();
    }

    @Override
    public Object createTeamPacket(TabListTeam team) {
        PacketContainer packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);

        packet.getIntegers().write(0, TeamMode.CREATE.getMode());
        packet.getStrings().write(0, team.getTeamName());

        packet.getSpecificModifier(Collection.class).write(0, team.getEntries());

        //https://www.spigotmc.org/threads/help-with-protocollib-packet-formating.528259/
        Optional<InternalStructure> structureOptional = packet.getOptionalStructures().read(0);
        if (structureOptional.isPresent()) {
            InternalStructure structure = structureOptional.get();

            structure.getChatComponents().write(0, WrappedChatComponent.fromLegacyText(team.getDisplayName()));
            structure.getChatComponents().write(1, WrappedChatComponent.fromLegacyText(team.getPrefix()));
            structure.getChatComponents().write(2, WrappedChatComponent.fromLegacyText(team.getSuffix()));

            structure.getStrings().write(0, team.getNameTagVisibility().getId());
            structure.getStrings().write(1, team.getCollisionRule().getId());

            structure.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat")).write(0, team.getColor());

            structure.getIntegers().write(0, team.getPackOptionData());

            packet.getOptionalStructures().write(0, Optional.of(structure));
        }

        return packet.getHandle();
    }

    @Override
    public String protocolVersion() {
        return "v1.17";
    }

    @Override
    public int[] supportedVersions() {
        return new int[]{17, 18, 19, 20};
    }

}
