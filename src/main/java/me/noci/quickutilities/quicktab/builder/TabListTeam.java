package me.noci.quickutilities.quicktab.builder;

import com.cryptomorin.xseries.ReflectionUtils;
import lombok.Getter;
import me.noci.quickutilities.quicktab.utils.CollisionRule;
import me.noci.quickutilities.quicktab.utils.NameTagVisibility;
import me.noci.quickutilities.utils.Require;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class TabListTeam {

    private final Player owner;
    private final int sortID;

    @Getter private final List<String> entries;
    @Getter private final String displayName;
    @Getter private final String prefix;
    @Getter private final String suffix;
    @Getter private final ChatColor color;
    @Getter private final NameTagVisibility nameTagVisibility;
    @Getter private final CollisionRule collisionRule;
    @Getter private final boolean allowFriendlyFire;
    @Getter private final boolean seeFriendlyInvisible;

    public TabListTeam(Player player, int sortID, String displayName, List<String> entries, String prefix, String suffix,
                       ChatColor color, NameTagVisibility nameTagVisibility, CollisionRule collisionRule,
                       boolean allowFriendlyFire, boolean seeFriendlyInvisible) {
        this.owner = player;
        this.sortID = sortID;
        this.displayName = Require.nonBlank(displayName).orElse(player.getDisplayName());
        this.entries = entries;
        this.prefix = chopString(prefix);
        this.suffix = chopString(suffix);
        this.color = color;
        this.nameTagVisibility = nameTagVisibility;
        this.collisionRule = collisionRule;
        this.allowFriendlyFire = allowFriendlyFire;
        this.seeFriendlyInvisible = seeFriendlyInvisible;
    }


    public String getTeamName() {
        String teamName = String.format("%04d", this.sortID);
        teamName += this.owner.getUniqueId().toString();
        return chopString(teamName);
    }

    public int getPackOptionData() {
        int data = 0;
        if (allowFriendlyFire) {
            data |= 1;
        }
        if (seeFriendlyInvisible) {
            data |= 2;
        }
        return data;
    }

    private String chopString(String string) {
        if (ReflectionUtils.supports(18) || string.length() <= 16) return string;
        return string.substring(0, 16);
    }

}
