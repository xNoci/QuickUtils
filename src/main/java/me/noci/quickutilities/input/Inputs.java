package me.noci.quickutilities.input;

import me.noci.quickutilities.input.functions.InputExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Inputs {

    public static Input chat(JavaPlugin plugin, Player player, String notify, InputExecutor inputExecutor) {
        return new PlayerChatInput(plugin, player, notify, inputExecutor);
    }

    public static Input chat(JavaPlugin plugin, Player player, String notify, String cancelInput, InputExecutor inputExecutor) {
        return new PlayerChatInput(plugin, player, notify, cancelInput, inputExecutor);
    }

    public static Input title(JavaPlugin plugin, Player player, String title, InputExecutor inputExecutor) {
        return new TitledPlayerChatInput(plugin, player, inputExecutor, title);
    }

    public static Input title(JavaPlugin plugin, Player player, String title, String subtitle, InputExecutor inputExecutor) {
        return new TitledPlayerChatInput(plugin, player, inputExecutor, title, subtitle);
    }

    public static Input title(JavaPlugin plugin, Player player, String cancelString, String title, String subtitle, InputExecutor inputExecutor) {
        return new TitledPlayerChatInput(plugin, player, cancelString, inputExecutor, title, subtitle);
    }

}
