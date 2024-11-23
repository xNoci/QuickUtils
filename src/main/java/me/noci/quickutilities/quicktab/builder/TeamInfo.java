package me.noci.quickutilities.quicktab.builder;

import com.cryptomorin.xseries.reflection.XReflection;
import me.noci.quickutilities.quicktab.utils.CollisionRule;
import me.noci.quickutilities.quicktab.utils.NameTagVisibility;
import me.noci.quickutilities.utils.Require;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public record TeamInfo(List<String> entries, String teamName, String displayName,
                       String prefix, String suffix, ChatColor color, NameTagVisibility nameTagVisibility,
                       CollisionRule collisionRule, int packOptionData) {

    public TeamInfo(Player player, int sortID, String displayName, List<String> entries, String prefix, String suffix,
                    ChatColor color, NameTagVisibility nameTagVisibility, CollisionRule collisionRule,
                    boolean allowFriendlyFire, boolean seeFriendlyInvisible) {
        this(entries, generateTeamName(player, sortID), Require.nonBlank(displayName).orElse(player.getDisplayName()),
                chopString(prefix), chopString(suffix), color,
                nameTagVisibility, collisionRule, createPackOptionData(allowFriendlyFire, seeFriendlyInvisible));
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
        if (XReflection.supports(18) || string.length() <= 16) return string;
        return string.substring(0, 16);
    }

}
