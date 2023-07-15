package me.noci.quickutilities.quicktab.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.noci.quickutilities.quicktab.builder.TabListTeam;
import org.bukkit.ChatColor;

import java.util.Collection;

public class TeamPacketHandlerV1_13 implements TeamPacketHandler {

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

        packet.getIntegers().write(1, team.getPackOptionData());

        packet.getStrings().write(1, team.getNameTagVisibility().getId());
        packet.getStrings().write(2, team.getCollisionRule().getId());

        packet.getChatComponents().write(0, WrappedChatComponent.fromLegacyText(team.getDisplayName()));
        packet.getChatComponents().write(1, WrappedChatComponent.fromLegacyText(team.getPrefix()));
        packet.getChatComponents().write(2, WrappedChatComponent.fromLegacyText(team.getSuffix()));

        packet.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat")).write(0, team.getColor());

        packet.getSpecificModifier(Collection.class).write(0, team.getEntries());

        return packet.getHandle();
    }

    @Override
    public String protocolVersion() {
        return "v1.13";
    }

}
