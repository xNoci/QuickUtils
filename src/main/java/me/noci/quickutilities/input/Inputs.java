package me.noci.quickutilities.input;

import me.noci.quickutilities.input.functions.InputExecutor;
import me.noci.quickutilities.input.sign.SignInputBuilder;
import org.bukkit.entity.Player;

import java.util.List;

public class Inputs {

    public static Input chat(Player player, String notify, InputExecutor inputExecutor) {
        return new PlayerChatInput(player, notify, inputExecutor);
    }

    public static Input chat(Player player, String notify, String cancelInput, InputExecutor inputExecutor) {
        return new PlayerChatInput(player, notify, cancelInput, inputExecutor);
    }

    public static Input title(Player player, String title, InputExecutor inputExecutor) {
        return new TitledPlayerChatInput(player, inputExecutor, title);
    }

    public static Input title(Player player, String title, String subtitle, InputExecutor inputExecutor) {
        return new TitledPlayerChatInput(player, inputExecutor, title, subtitle);
    }

    public static Input title(Player player, String cancelString, String title, String subtitle, InputExecutor inputExecutor) {
        return new TitledPlayerChatInput(player, cancelString, inputExecutor, title, subtitle);
    }

    public static Input sign(Player player, int inputLine, InputExecutor inputExecutor) {
        return sign(player, inputLine, List.of(), inputExecutor);
    }

    public static Input sign(Player player, int inputLine, List<String> signLines, InputExecutor inputExecutor) {
        return sign()
                .inputLine(inputLine)
                .signLines(signLines)
                .input(inputExecutor)
                .build(player);
    }

    public static SignInputBuilder sign() {
        return new SignInputBuilder();
    }

}
