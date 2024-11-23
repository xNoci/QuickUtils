package me.noci.quickutilities.quicktab.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.noci.quickutilities.quicktab.builder.TeamInfo;
import org.bukkit.ChatColor;

import java.util.Collection;

public class TeamPacketHandlerV1_13 implements TeamPacketHandler {

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

        packet.getIntegers().write(1, team.packOptionData());

        packet.getStrings().write(1, team.nameTagVisibility().getId());
        packet.getStrings().write(2, team.collisionRule().getId());

        packet.getChatComponents().write(0, WrappedChatComponent.fromLegacyText(team.displayName()));
        packet.getChatComponents().write(1, WrappedChatComponent.fromLegacyText(team.prefix()));
        packet.getChatComponents().write(2, WrappedChatComponent.fromLegacyText(team.suffix()));

        packet.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat")).write(0, team.color());

        packet.getSpecificModifier(Collection.class).write(0, team.entries());

        return packet.getHandle();
    }

    @Override
    public int version() {
        return 13;
    }

}
