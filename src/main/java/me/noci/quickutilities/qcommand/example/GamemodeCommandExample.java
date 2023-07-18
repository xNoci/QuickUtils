package me.noci.quickutilities.qcommand.example;

import me.noci.quickutilities.QuickUtils;
import me.noci.quickutilities.qcommand.QCommand;
import me.noci.quickutilities.qcommand.annotation.Command;
import me.noci.quickutilities.qcommand.annotation.CommandPermission;
import me.noci.quickutilities.qcommand.annotation.FallbackCommand;
import me.noci.quickutilities.qcommand.annotation.SubCommand;
import me.noci.quickutilities.qcommand.mappings.CommandMapping;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GamemodeCommandExample extends QCommand {

    static {
        CommandMapping.registerPlayerMapping(SamplePlayer.class, SamplePlayer::new);
        CommandMapping.registerArgumentMapping(SamplePlayer.class, argument -> new SamplePlayer(Bukkit.getPlayer(argument)));
    }

    public GamemodeCommandExample() {
        super(QuickUtils.instance(), "gamemode", List.of("gm"), "Change the gamemode", "/gamemode <type> [player]");
    }

    @Command // Should be /gamemode <gamemode>
    private void changeGamemode(SamplePlayer player, GameMode gameMode) { //SUPPORT Optional<GameMode> - With every argument type
        if(gameMode == null) {
            playerFallback(player.player);
            return;
        }
        player.player.sendMessage("Changed your gamemode to: " + gameMode);
        player.player.setGameMode(gameMode);
    }

    @SubCommand(path = "test") // Should be /gamemode test <gamemode>
    private void testGamemode(Player player, GameMode gameMode) {
        if(gameMode == null) {
            playerFallback(player);
            return;
        }
        player.sendMessage("Changed your gamemode to: " + gameMode);
        player.setGameMode(gameMode);
    }

    @SubCommand(path = {"test", "12"}) // Should be /gamemode test 12 <gamemode>
    private void test12Gamemode(Player player, GameMode gameMode) {
        if(gameMode == null) {
            playerFallback(player);
            return;
        }
        player.sendMessage("Changed your gamemode to: " + gameMode);
        player.setGameMode(gameMode);
    }

    @Command
    @CommandPermission("command.gamemode") // Should be /gamemode <gamemode> <target>
    private void changeOther(Player player, GameMode gameMode, SamplePlayer target) {
        if(target == null || target.player == null || !target.player.isOnline()) {
            player.sendMessage("Could not find your requested player");
            return;
        }

        if(gameMode == null) {
            playerFallback(player);
            return;
        }

        if(player.getUniqueId().equals(target.player.getUniqueId())) {
            player.sendMessage("Cannot target yourself");
            return;
        }

        player.sendMessage("Changed gamemode for %s to %s".formatted(target.player.getName(), gameMode));
        target.player.sendMessage("Your gamemode was changed to: " + gameMode);
        target.player.setGameMode(gameMode);
    }

    @FallbackCommand
    private void playerFallback(Player player) {
        player.sendMessage(getUsage());
    }

    @FallbackCommand
    private void commandSenderFallback(CommandSender sender) {
        sender.sendMessage("This command is only for players");
    }


    private static class SamplePlayer {

        private final Player player;

        private SamplePlayer(Player player) {
            this.player = player;
        }
    }


}
