package me.noci.quickutilities.quicktab.builder;

import com.cryptomorin.xseries.reflection.XReflection;
import com.google.common.collect.Lists;
import me.noci.quickutilities.quicktab.utils.CollisionRule;
import me.noci.quickutilities.quicktab.utils.NameTagVisibility;
import me.noci.quickutilities.quicktab.utils.TabListCondition;
import me.noci.quickutilities.quicktab.utils.TabListFunction;
import me.noci.quickutilities.quicktab.utils.ConditionalComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class DefaultQuickTabBuilder implements QuickTabBuilder {

    private static final NamedTextColor DEFAULT_COLOR = NamedTextColor.WHITE;

    private final ConditionalComponent prefix = new ConditionalComponent();
    private final ConditionalComponent suffix = new ConditionalComponent();
    private TabListFunction<Component> displayName = null;
    private TabListFunction<Integer> sortID = (player, target) -> Integer.MAX_VALUE; //Default sorted as last
    private TabListFunction<String[]> entries = (player, target) -> new String[] { target.getName() };
    //Default only at the target to the team
    //If the server does not support 1.13 color is set to null because it will be used as the color of the end of the prefix
    private TabListFunction<NamedTextColor> color =
            (player, target) -> XReflection.v(1, 13, DEFAULT_COLOR).orElse((NamedTextColor) null);
    private TabListFunction<NameTagVisibility> nameTagVisibility = (player, target) -> NameTagVisibility.ALWAYS;
    private TabListFunction<CollisionRule> collisionRule = (player, target) -> CollisionRule.ALWAYS;
    private TabListFunction<Boolean> allowFriendlyFire = (player, target) -> true;
    private TabListFunction<Boolean> seeFriendlyInvisible = (player, target) -> true;

    public DefaultQuickTabBuilder() {
    }

    @Override
    public QuickTabBuilder sortID(TabListFunction<Integer> sortID) {
        this.sortID = sortID;
        return this;
    }

    @Override
    public QuickTabBuilder entries(TabListFunction<String[]> entries) {
        this.entries = entries;
        return this;
    }

    @Override
    public QuickTabBuilder displayName(TabListFunction<Component> displayName) {
        this.displayName = displayName;
        return this;
    }

    @Override
    public QuickTabBuilder prefix(TabListFunction<Component> prefix, TabListCondition<Player> condition) {
        this.prefix.setExtensionFunction(prefix);
        this.prefix.setCondition(condition);
        return this;
    }

    @Override
    public QuickTabBuilder suffix(TabListFunction<Component> suffix, TabListCondition<Player> condition) {
        this.suffix.setExtensionFunction(suffix);
        this.suffix.setCondition(condition);
        return this;
    }

    @Override
    public QuickTabBuilder color(TabListFunction<NamedTextColor> color) {
        this.color = color;
        return this;
    }

    @Override
    public QuickTabBuilder allowFriendlyFire(TabListFunction<Boolean> allowFriendlyFire) {
        this.allowFriendlyFire = allowFriendlyFire;
        return this;
    }

    @Override
    public QuickTabBuilder seeFriendlyInvisible(TabListFunction<Boolean> seeFriendlyInvisible) {
        this.seeFriendlyInvisible = seeFriendlyInvisible;
        return this;
    }

    @Override
    public QuickTabBuilder nameTagVisibility(TabListFunction<NameTagVisibility> nameTagVisibility) {
        this.nameTagVisibility = nameTagVisibility;
        return this;
    }

    @Override
    public QuickTabBuilder collisionRule(TabListFunction<CollisionRule> collisionRule) {
        this.collisionRule = collisionRule;
        return this;
    }

    @Override
    public TeamInfo build(Player player, Player target) {
        int sortID = Math.min(this.sortID.apply(player, target), 999);
        List<String> entries = Lists.newArrayList(this.entries.apply(player, target));

        Component displayName = this.displayName != null ? this.displayName.apply(player, target) : null;
        Component prefix = this.prefix.apply(player, target);
        Component suffix = this.suffix.apply(player, target);
        NamedTextColor color = this.color.apply(player, target);
        NameTagVisibility nameTagVisibility = this.nameTagVisibility.apply(player, target);
        CollisionRule collisionRule = this.collisionRule.apply(player, target);
        boolean allowFriendlyFire = this.allowFriendlyFire.apply(player, target);
        boolean seeFriendlyInvisible = this.seeFriendlyInvisible.apply(player, target);

        return new TeamInfo(target, sortID, displayName, entries, prefix, suffix, color == null ? DEFAULT_COLOR : color,
                nameTagVisibility, collisionRule, allowFriendlyFire, seeFriendlyInvisible);
    }

}
