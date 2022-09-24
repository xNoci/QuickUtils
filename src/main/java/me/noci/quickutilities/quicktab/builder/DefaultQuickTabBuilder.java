package me.noci.quickutilities.quicktab.builder;

import com.google.common.collect.Lists;
import me.noci.quickutilities.quicktab.utils.*;
import me.noci.quickutilities.utils.ReflectionUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class DefaultQuickTabBuilder implements QuickTabBuilder {

    private static final ChatColor DEFAULT_COLOR = ChatColor.WHITE;

    private final TeamPrefix prefix = new TeamPrefix();
    private final TeamSuffix suffix = new TeamSuffix();
    private TabListFunction<Player, Integer> sortID = (player, target) -> Integer.MAX_VALUE; //Default sorted as last
    private TabListFunction<Player, String[]> entries = (player, target) -> new String[]{target.getName()}; //Default only at the target to the team
    //If the server does not support 1.13 color is set to null because it will be used as the color of the end of the prefix
    private TabListFunction<Player, ChatColor> color = (player, target) -> ReflectionUtils.supports(13) ? DEFAULT_COLOR : null;
    private TabListFunction<Player, NameTagVisibility> nameTagVisibility = (player, target) -> NameTagVisibility.ALWAYS;
    private TabListFunction<Player, CollisionRule> collisionRule = (player, target) -> CollisionRule.ALWAYS;
    private TabListFunction<Player, Boolean> allowFriendlyFire = (player, target) -> true;
    private TabListFunction<Player, Boolean> seeFriendlyInvisible = (player, target) -> true;

    public DefaultQuickTabBuilder() {
    }

    @Override
    public QuickTabBuilder sortID(TabListFunction<Player, Integer> sortID) {
        this.sortID = sortID;
        return this;
    }

    @Override
    public QuickTabBuilder entries(TabListFunction<Player, String[]> entries) {
        this.entries = entries;
        return this;
    }

    @Override
    public QuickTabBuilder prefix(TabListFunction<Player, String> prefix, TabListCondition<Player> condition) {
        this.prefix.setPrefixFunction(prefix);
        this.prefix.setCondition(condition);
        return this;
    }

    @Override
    public QuickTabBuilder suffix(TabListFunction<Player, String> suffix, TabListCondition<Player> condition) {
        this.suffix.setSuffixFunction(suffix);
        this.suffix.setCondition(condition);
        return this;
    }

    @Override
    public QuickTabBuilder color(TabListFunction<Player, ChatColor> color) {
        this.color = color;
        return this;
    }

    @Override
    public QuickTabBuilder allowFriendlyFire(TabListFunction<Player, Boolean> allowFriendlyFire) {
        this.allowFriendlyFire = allowFriendlyFire;
        return this;
    }

    @Override
    public QuickTabBuilder seeFriendlyInvisible(TabListFunction<Player, Boolean> seeFriendlyInvisible) {
        this.seeFriendlyInvisible = seeFriendlyInvisible;
        return this;
    }

    @Override
    public QuickTabBuilder nameTagVisibility(TabListFunction<Player, NameTagVisibility> nameTagVisibility) {
        this.nameTagVisibility = nameTagVisibility;
        return this;
    }

    @Override
    public QuickTabBuilder collisionRule(TabListFunction<Player, CollisionRule> collisionRule) {
        this.collisionRule = collisionRule;
        return this;
    }

    @Override
    public TabListTeam build(Player player, Player target) {
        int sortID = Math.min(this.sortID.apply(player, target), 999);
        List<String> entries = Lists.newArrayList(this.entries.apply(player, target));

        ChatColor chatColor = this.color.apply(player, target);
        String prefix = convertPrefix(player, target, chatColor);
        String suffix = this.suffix.getSuffix(player, target);
        ChatColor color = this.color.apply(player, target);
        NameTagVisibility nameTagVisibility = this.nameTagVisibility.apply(player, target);
        CollisionRule collisionRule = this.collisionRule.apply(player, target);
        boolean allowFriendlyFire = this.allowFriendlyFire.apply(player, target);
        boolean seeFriendlyInvisible = this.seeFriendlyInvisible.apply(player, target);

        return new TabListTeam(target, sortID, entries, prefix, suffix, color == null ? DEFAULT_COLOR : color, nameTagVisibility, collisionRule, allowFriendlyFire, seeFriendlyInvisible);
    }

    /**
     * Create a Prefix with the {@link TeamPrefix} field.
     *
     * @param player The player which tab list will be set
     * @param target The current player that will be changed
     * @param color  The {@link QuickTabBuilder#color(TabListFunction) ChatColor} which is used
     * @return The default {@link QuickTabBuilder#prefix(TabListFunction) prefix} when the
     * current server version is grater or equals to 1.13, or {@link QuickTabBuilder#color(TabListFunction) ChatColor}
     * is set to null or the prefix ends with a {@link #endsWithColorCode(String) color code}.
     * Otherwise, returns a new prefix which ends with the given {@link QuickTabBuilder#color(TabListFunction) ChatColor}.
     * This will limit the actual prefix length to 14 characters.
     */
    private String convertPrefix(Player player, Player target, ChatColor color) {
        String prefix = this.prefix.getPrefix(player, target);

        if (ReflectionUtils.supports(13) || color == null) return prefix;
        if (endsWithColorCode(prefix)) return prefix;
        if (prefix.length() <= 14) return prefix + color;
        return prefix.substring(0, 14) + color;
    }

    /**
     * Checks whether the given input string ends with a ChatColor code or not.
     *
     * @param input The string to check
     * @return true if the string ends with a color code or reset code, otherwise returns false
     */
    private boolean endsWithColorCode(String input) {
        int length = input.length();
        if (length < 2) return false;
        if (input.charAt(length - 2) != 167) return false;
        char code = input.charAt(length - 1);
        ChatColor color = ChatColor.getByChar(code);
        if (color == null) return false;
        return color.isColor() || color.equals(ChatColor.RESET);
    }

}
