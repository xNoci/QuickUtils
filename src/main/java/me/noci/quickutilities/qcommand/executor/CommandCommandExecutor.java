package me.noci.quickutilities.qcommand.executor;

import org.bukkit.command.CommandSender;

public class CommandCommandExecutor implements CommandExecutor<CommandCommandExecutor> {
    @Override
    public boolean hasPermission(CommandSender sender) {
        return false;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

    }

    @Override
    public boolean matches(CommandSender sender, String[] args) {
        return false;
    }

    @Override
    public MatchPriority compareMatch(CommandCommandExecutor toCompare, CommandSender sender, String[] args) {
        return null;
    }
}
