package me.noci.quickutilities.quicktab.utils;

import lombok.Setter;
import org.bukkit.entity.Player;

public class TeamSuffix {

    @Setter private TabListCondition<Player> condition = (player, target) -> true;
    @Setter private TabListFunction<Player, String> suffixFunction = (player, target) -> "";

    public String getSuffix(Player player, Player target) {
        if (!condition.test(player, target)) return "";
        return suffixFunction.apply(player, target);
    }

}
