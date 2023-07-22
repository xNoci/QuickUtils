package me.noci.quickutilities.quicktab.utils;

import lombok.Setter;
import org.bukkit.entity.Player;

public class TeamExtension {

    @Setter private TabListCondition<Player> condition = (player, target) -> true;
    @Setter private TabListFunction<Player, String> extensionFunction = (player, target) -> "";

    public String getExtension(Player player, Player target) {
        if (!condition.test(player, target)) return "";
        return extensionFunction.apply(player, target);
    }

}
