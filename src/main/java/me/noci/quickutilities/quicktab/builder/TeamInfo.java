package me.noci.quickutilities.quicktab.builder;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.cryptomorin.xseries.reflection.XReflection;
import me.noci.quickutilities.quicktab.utils.CollisionRule;
import me.noci.quickutilities.quicktab.utils.NameTagVisibility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public record TeamInfo(List<String> entries, String teamName, Component displayName, Component prefix, Component suffix,
                       EnumWrappers.ChatFormatting color, NameTagVisibility nameTagVisibility,
                       CollisionRule collisionRule,
                       int packOptionData) {

    public TeamInfo(Player player, int sortID, Component displayName, List<String> entries, Component prefix,
                    Component suffix,
                    NamedTextColor color, NameTagVisibility nameTagVisibility, CollisionRule collisionRule,
                    boolean allowFriendlyFire, boolean seeFriendlyInvisible) {
        this(entries, generateTeamName(player, sortID),
                Optional.ofNullable(displayName).orElse(player.displayName()), prefix, suffix, convertColor(color),
                nameTagVisibility,
                collisionRule, createPackOptionData(allowFriendlyFire, seeFriendlyInvisible));
    }

    private static int createPackOptionData(boolean allowFriendlyFire, boolean seeFriendlyInvisible) {
        int data = 0;
        if (allowFriendlyFire) {
            data |= 1;
        }
        if (seeFriendlyInvisible) {
            data |= 2;
        }
        return data;
    }

    private static String generateTeamName(Player player, int sortId) {
        String teamName = String.format("%04d", sortId);
        teamName += player.getUniqueId().toString();
        return chopString(teamName);
    }

    private static String chopString(String string) {
        if (XReflection.supports(1, 18) || string.length() <= 16) {
            return string;
        }
        return string.substring(0, 16);
    }

    private static EnumWrappers.ChatFormatting convertColor(NamedTextColor textColor) {
        if (textColor == null) {
            return EnumWrappers.ChatFormatting.RESET;
        }

        try {
            return EnumWrappers.ChatFormatting.valueOf(textColor.toString().toUpperCase());
        } catch (IllegalArgumentException e) {
            return EnumWrappers.ChatFormatting.RESET;
        }
    }

}
