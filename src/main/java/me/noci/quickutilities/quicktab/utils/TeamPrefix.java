package me.noci.quickutilities.quicktab.utils;

import lombok.Setter;
import org.bukkit.entity.Player;

public class TeamPrefix {

    @Setter private TabListCondition<Player> condition = (player, target) -> true;
    @Setter private TabListFunction<Player, String> prefixFunction = (player, target) -> "";

    public String getPrefix(Player player, Player target) {
        if (!condition.test(player, target)) return "";
        return prefixFunction.apply(player, target);
    }

}
