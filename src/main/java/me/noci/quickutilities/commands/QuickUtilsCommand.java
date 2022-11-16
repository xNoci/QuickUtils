package me.noci.quickutilities.commands;

import me.noci.quickutilities.quickcommand.QuickCommand;
import me.noci.quickutilities.quickcommand.annotations.DefaultCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class QuickUtilsCommand extends QuickCommand {

    public QuickUtilsCommand(JavaPlugin plugin) {
        super(plugin, "quickutils");
        setDescription("Shows information about this plugin.");
        setUsage("§cUsage: /quickutils");

        autoRegister();
    }

    @DefaultCommand
    public void handleExecute(CommandSender sender) {
        sender.sendMessage("§7The version of §9%s §7is currently §9%s§7.".formatted(getPlugin().getName(), getPlugin().getDescription().getVersion()));
    }

}
