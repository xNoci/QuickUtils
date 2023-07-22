package me.noci.quickutilities.quickcommand.example;

import me.noci.quickutilities.QuickUtils;
import me.noci.quickutilities.quickcommand.QuickCommand;
import me.noci.quickutilities.quickcommand.annotation.*;
import me.noci.quickutilities.quickcommand.mappings.CommandMapping;
import me.noci.quickutilities.quickcommand.mappings.spacedvalues.SpacedCharArray;
import me.noci.quickutilities.quickcommand.mappings.spacedvalues.SpacedString;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public final class GamemodeCommandExample extends QuickCommand {

    static {
        CommandMapping.registerPlayerMapping(SamplePlayer.class, SamplePlayer::new);
        CommandMapping.registerArgumentMapping(SamplePlayer.class, argument -> new SamplePlayer(Bukkit.getPlayer(argument)));
        CommandMapping.registerArgumentMapping(SampleWorld.class, argument -> {
            World world = Bukkit.getWorld(argument);
            return world != null ? new SampleWorld(world) : null;
        });
    }

    private GamemodeCommandExample() {
        super(QuickUtils.instance(), "gamemode", List.of("gm"), "Change the gamemode", "/gamemode <type> [player]");
        autoRegister();
    }

    @Command // Should be /gamemode <gamemode>
    private void changeGamemode(SamplePlayer player, GameMode gameMode) {
        System.out.println("First method");
        if (gameMode == null) {
            playerFallback(player.player);
            return;
        }
        player.player.sendMessage("Changed your gamemode to: " + gameMode);
        player.player.setGameMode(gameMode);
    }

    @SubCommand(path = "test") // Should be /gamemode test <gamemode>
    private void testGamemode(Player player, GameMode gameMode) {
        System.out.println("Second method");
        if (gameMode == null) {
            playerFallback(player);
            return;
        }
        player.sendMessage("Changed your gamemode to: " + gameMode);
        player.setGameMode(gameMode);
    }

    @SubCommand(path = "test") // Should be /gamemode test
    private void testGamemode(Player player) {
        System.out.println("test method");
    }

    @SubCommand(path = "test2") // Should be /gamemode test2
    private void test2Gamemode(Player player) {
        System.out.println("test2 method");
    }

    @SubCommand(path = "test2") // Should be /gamemode test2
    private void test2Gamemode(CommandSender player) {
        System.out.println("test2 method console");
    }

    @SubCommand(path = "test3") // Should be /gamemode test3
    private void test3Gamemode(CommandSender player) {
        System.out.println("test3 method console");
    }

    @SubCommand(path = {"test", "12"}) // Should be /gamemode test 12 <gamemode>
    private void test12Gamemode(Player player, @IgnoreStrictEnum GameMode gameMode) {
        System.out.println("Third method");
        if (gameMode == null) { //BECAUSE @IgnoreStrictEnum is used
            playerFallback(player);
            return;
        }
        player.sendMessage("Changed your gamemode to: " + gameMode);
        player.setGameMode(gameMode);
    }

    @SubCommand(path = {"string"}) // Should be /gamemode string [string]
    private void test12Gamemode(Player player, String string) {
        System.out.println("Fourth method");
        player.sendMessage(string);
    }

    @SubCommand(path = {"string2"}) // Should be /gamemode string [string1] [string2]
    private void test12Gamemode(Player player, String string1, String string2) {
        System.out.println("Fifth method");
        player.sendMessage(string1 + " - " + string2);
    }

    @SubCommand(path = "string3")
    private void test3(CommandSender sender, SpacedString string) {
        sender.sendMessage(string.value());
    }

    @SubCommand(path = "string4")
    private void test4(CommandSender sender, String s, SpacedString string) {
        sender.sendMessage(s + " - " + string);
    }

    @SubCommand(path = "string5")
    private void test5(CommandSender sender, String s, double doesCompile, SpacedCharArray string) {
        sender.sendMessage(s + ":" + doesCompile + " - " + string);
    }

    @SubCommand(path = "string5")
    private void test5(CommandSender sender, String s, SpacedString string, double doseNotCompileNow) {
        sender.sendMessage(s + " - " + string);
    }

    @SubCommand(path = "string6")
    private void test6(CommandSender sender, String s) {
        sender.sendMessage(s);
    }

    @SubCommand(path = "world")
    private void test(CommandSender sender, @MatchNull SampleWorld world) {
        sender.sendMessage("World: " + (world == null));
    }

    @Command
    @CommandPermission("command.gamemode") // Should be /gamemode <gamemode> <target>
    private void changeOther(Player player, GameMode gameMode, SamplePlayer target) {
        System.out.println("Sixth method");
        if (target == null || target.player == null || !target.player.isOnline()) {
            player.sendMessage("Could not find your requested player");
            return;
        }

        if (gameMode == null) {
            playerFallback(player);
            return;
        }

        if (player.getUniqueId().equals(target.player.getUniqueId())) {
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


    private record SamplePlayer(Player player) {
    }

    private record SampleWorld(World world) {
    }


}
