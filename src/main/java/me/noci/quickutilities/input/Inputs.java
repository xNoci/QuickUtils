package me.noci.quickutilities.input;

import me.noci.quickutilities.input.functions.InputExecutor;
import me.noci.quickutilities.input.sign.SignInputBuilder;
import me.noci.quickutilities.utils.Legacy;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class Inputs {

    public static Input chat(Player player, String notify, InputExecutor inputExecutor) {
        return new PlayerChatInput(player, notify, inputExecutor);
    }

    public static Input chat(Player player, Component notify, InputExecutor inputExecutor) {
        return new PlayerChatInput(player, Legacy.serialize(notify), inputExecutor);
    }

    public static Input chat(Player player, String notify, String cancelInput, InputExecutor inputExecutor) {
        return new PlayerChatInput(player, notify, cancelInput, inputExecutor);
    }

    public static Input chat(Player player, Component notify, Component cancelInput, InputExecutor inputExecutor) {
        return new PlayerChatInput(player, Legacy.serialize(notify), Legacy.serialize(cancelInput), inputExecutor);
    }

    public static Input title(Player player, String title, InputExecutor inputExecutor) {
        return new TitledPlayerChatInput(player, inputExecutor, title);
    }

    public static Input title(Player player, Component title, InputExecutor inputExecutor) {
        return new TitledPlayerChatInput(player, inputExecutor, Legacy.serialize(title));
    }

    public static Input title(Player player, String title, String subtitle, InputExecutor inputExecutor) {
        return new TitledPlayerChatInput(player, inputExecutor, title, subtitle);
    }

    public static Input title(Player player, Component title, Component subtitle, InputExecutor inputExecutor) {
        return new TitledPlayerChatInput(player, inputExecutor, Legacy.serialize(title), Legacy.serialize(subtitle));
    }

    public static Input title(Player player, String cancelString, String title, String subtitle, InputExecutor inputExecutor) {
        return new TitledPlayerChatInput(player, cancelString, inputExecutor, title, subtitle);
    }

    public static Input title(Player player, Component cancelString, Component title, Component subtitle, InputExecutor inputExecutor) {
        return new TitledPlayerChatInput(player, Legacy.serialize(cancelString), inputExecutor, Legacy.serialize(title), Legacy.serialize(subtitle));
    }

    public static Input sign(Player player, int inputLine, InputExecutor inputExecutor) {
        return sign(player, inputLine, List.of(), inputExecutor);
    }

    public static Input sign(Player player, int inputLine, List<String> signLines, InputExecutor inputExecutor) {
        return sign()
                .inputLine(inputLine)
                .setSignLines(signLines)
                .input(inputExecutor)
                .build(player);
    }

    public static Input signComponent(Player player, int inputLine, List<Component> signLines, InputExecutor inputExecutor) {
        return sign()
                .inputLine(inputLine)
                .setSignLines(signLines.stream().map(Legacy::serialize).collect(Collectors.toList()))
                .input(inputExecutor)
                .build(player);
    }

    public static SignInputBuilder sign() {
        return new SignInputBuilder();
    }

}
