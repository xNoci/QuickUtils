package me.noci.quickutilities.qcommand.executor;

import org.bukkit.command.CommandSender;

public class FallbackCommandExecutor implements CommandExecutor<FallbackCommandExecutor> {
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
    public MatchPriority compareMatch(FallbackCommandExecutor toCompare, CommandSender sender, String[] args) {
        return null;
    }
}
