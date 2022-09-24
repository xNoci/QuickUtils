package me.noci.quickutilities.quicktab.builder;


import me.noci.quickutilities.quicktab.utils.CollisionRule;
import me.noci.quickutilities.quicktab.utils.NameTagVisibility;
import me.noci.quickutilities.quicktab.utils.TabListCondition;
import me.noci.quickutilities.quicktab.utils.TabListFunction;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

/**
 * This interface is used to create custom tab lists.
 */
public interface QuickTabBuilder {

    /**
     * The order in which players will be sorted. Lower number means the player will be higher up.
     * SortIDs greater than 999 are limited to 999.
     * <br> The {@link TabListFunction} will offer two players: the first one will be the player for which the tab list will be set; the second one is the target player - the first player will be included.
     *
     * @param sortID The sort order for the given player.
     * @return the used {@link QuickTabBuilder}
     */
    QuickTabBuilder sortID(TabListFunction<Player, Integer> sortID);

    /**
     * Adds the specified entry into this builder.
     * <br>This is optional, if not set only the target will be in this team.
     * <br> The {@link TabListFunction} will offer two players: the first one will be the player for which the tab list will be set; the second one is the target player - the first player will be included.
     *
     * @param entries The entries that are in the team of the given player.
     * @return the used {@link QuickTabBuilder}
     */
    QuickTabBuilder entries(TabListFunction<Player, String[]> entries);

    /**
     * This method set the prefix for the given player if the {@link TabListCondition condition} is true, otherwise the prefix will be empty.
     * <br>If the version is below <strong>1.18</strong> the max prefix length will be limited to 16 characters.
     * <br> The {@link TabListFunction} will offer two players: the first one will be the player for which the tab list will be set; the second one is the target player - the first player will be included.
     *
     * @param prefix    The string that will be in front of the given player name
     * @param condition if the condition equals to false the suffix will not be set
     * @return the used {@link QuickTabBuilder}
     */
    QuickTabBuilder prefix(TabListFunction<Player, String> prefix, TabListCondition<Player> condition);

    /**
     * This method will always set the prefix for the given player.
     * <br>If the version is below <strong>1.18</strong> the max prefix length will be limited to 16 characters.
     * <br>Equivalent to {@link #prefix(TabListFunction, TabListCondition) prefix(String, player -> true)}.
     * <br> The {@link TabListFunction} will offer two players: the first one will be the player for which the tab list will be set; the second one is the target player - the first player will be included.
     *
     * @param prefix The string that will be in front of the given player name
     * @return the used {@link QuickTabBuilder}
     */
    default QuickTabBuilder prefix(TabListFunction<Player, String> prefix) {
        return prefix(prefix, (player, target) -> true);
    }

    /**
     * This method set the suffix for the given player if the {@link TabListCondition condition} is true, otherwise the suffix will be empty.
     * <br>If the version is below <strong>1.18</strong> the max suffix length will be limited to 16 characters.
     * <br> The {@link TabListFunction} will offer two players: the first one will be the player for which the tab list will be set; the second one is the target player - the first player will be included.
     *
     * @param suffix    The string that will be behind the given player name
     * @param condition if the condition equals to false the suffix will not be set
     * @return the used {@link QuickTabBuilder}
     */
    QuickTabBuilder suffix(TabListFunction<Player, String> suffix, TabListCondition<Player> condition);

    /**
     * This method will always set the suffix for the given player.
     * <br>If the version is below <strong>1.18</strong> the max suffix length will be limited to 16 characters.
     * <br>Equivalent to {@link #suffix(TabListFunction, TabListCondition) suffix(String, player -> true)}.
     * <br> The {@link TabListFunction} will offer two players: the first one will be the player for which the tab list will be set; the second one is the target player - the first player will be included.
     *
     * @param suffix The string that will be behind the given player name
     * @return the used {@link QuickTabBuilder}
     */
    default QuickTabBuilder suffix(TabListFunction<Player, String> suffix) {
        return suffix(suffix, (player, target) -> true);
    }

    /**
     * This is only supported for version <strong>1.13+</strong>. In earlier versions the player name color was changed with the last color code of the {@link  #prefix(TabListFunction, TabListCondition) prefix}.
     * <br>However, in versions below <strong>1.13</strong> if set, this method will set the last color code of the prefix to the given color if possible. If the prefix ends with a color code or with the reset code, the color will not be set.
     * <br> This will limit the max length of the prefix to 14 characters.
     * <br> The {@link TabListFunction} will offer two players: the first one will be the player for which the tab list will be set; the second one is the target player - the first player will be included.
     *
     * @param color The color in which the name of the given player will be displayed
     * @return the used {@link QuickTabBuilder}
     */
    QuickTabBuilder color(TabListFunction<Player, ChatColor> color);

    /**
     * Set if friendly fire is allowed for all entries.
     * <br> The {@link TabListFunction} will offer two players: the first one will be the player for which the tab list will be set; the second one is the target player - the first player will be included.
     *
     * @param allowFriendlyFire Whether friendly fire is allowed or not
     * @return the used {@link QuickTabBuilder}
     */
    QuickTabBuilder allowFriendlyFire(TabListFunction<Player, Boolean> allowFriendlyFire);

    /**
     * Sets if invisible teammates can be seen.
     * <br> The {@link TabListFunction} will offer two players: the first one will be the player for which the tab list will be set; the second one is the target player - the first player will be included.
     *
     * @param seeFriendlyInvisible Whether the entries of this team can see invisible friendlies or not
     * @return the used {@link QuickTabBuilder}
     */
    QuickTabBuilder seeFriendlyInvisible(TabListFunction<Player, Boolean> seeFriendlyInvisible);

    /**
     * Sets the {@link NameTagVisibility} of this team.
     * <br> The {@link TabListFunction} will offer two players: the first one will be the player for which the tab list will be set; the second one is the target player - the first player will be included.
     *
     * @param nameTagVisibility The {@link NameTagVisibility} for the entries of this team.
     * @return the used {@link QuickTabBuilder}
     */
    QuickTabBuilder nameTagVisibility(TabListFunction<Player, NameTagVisibility> nameTagVisibility);

    /**
     * The collision rule is only available for version <strong>1.9+</strong>.
     * <br> The {@link TabListFunction} will offer two players: the first one will be the player for which the tab list will be set; the second one is the target player - the first player will be included.
     *
     * @param collisionRule The applied {@link CollisionRule} to the entry of this team.
     * @return the used {@link QuickTabBuilder}
     */
    QuickTabBuilder collisionRule(TabListFunction<Player, CollisionRule> collisionRule);

    /**
     * Build a {@link TabListTeam} to get the data structured and formatted.
     * <br> This will be used internally to create packets.
     *
     * @param player
     * @param target
     * @return
     */
    @ApiStatus.Internal
    TabListTeam build(Player player, Player target);

}
