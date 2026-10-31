package me.noci.quickutilities.quicktab.utils;

import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class ConditionalComponent {

    @Setter private TabListCondition<Player> condition = (player, target) -> true;
    @Setter private TabListFunction<Component> extensionFunction = (player, target) -> Component.empty();

    public Component apply(Player player, Player target) {
        if (!condition.test(player, target)) return Component.empty();
        return extensionFunction.apply(player, target);
    }

}
