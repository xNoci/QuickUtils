package me.noci.quickutilities.commands;

import me.noci.quickutilities.quickcommand.QuickCommand;
import me.noci.quickutilities.quickcommand.annotation.Command;
import me.noci.quickutilities.quickcommand.annotation.FallbackCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class QuickUtilsCommand extends QuickCommand {

    public QuickUtilsCommand(JavaPlugin plugin) {
        super(plugin, "quickutils", List.of(), "Shows information about this plugin.", "§cUsage: /quickutils");
        autoRegister();
    }

    @Command
    @FallbackCommand
    public void handleExecute(CommandSender sender) {
        sender.sendMessage("§7The version of §9%s §7is currently §9%s§7.".formatted(getPlugin().getName(), getPlugin().getDescription().getVersion()));
    }

}
