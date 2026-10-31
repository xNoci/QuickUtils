package me.noci.quickutilities.quicktab.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.noci.quickutilities.quicktab.builder.TeamInfo;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
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

        String displayName = JSONComponentSerializer.json().serialize(team.displayName());
        String prefix = JSONComponentSerializer.json().serialize(team.prefix());
        String suffix = JSONComponentSerializer.json().serialize(team.suffix());

        packet.getChatComponents().write(0, WrappedChatComponent.fromJson(displayName));
        packet.getChatComponents().write(1, WrappedChatComponent.fromJson(prefix));
        packet.getChatComponents().write(2, WrappedChatComponent.fromJson(suffix));

        packet.getModifier().withType(EnumWrappers.getChatFormattingClass())
                .write(0, EnumWrappers.getChatFormattingConverter().getGeneric(team.color()));

        packet.getSpecificModifier(Collection.class).write(0, team.entries());

        return packet.getHandle();
    }

    @Override
    public int major() {
        return 1;
    }

    @Override
    public int minor() {
        return 13;
    }
}
