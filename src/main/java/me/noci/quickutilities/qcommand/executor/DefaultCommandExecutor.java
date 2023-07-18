package me.noci.quickutilities.qcommand.executor;

import me.noci.quickutilities.quickcommand.annotations.CommandPermission;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;

public class DefaultCommandExecutor extends BaseCommandExecutor<DefaultCommandExecutor> {

    public DefaultCommandExecutor(Method method, CommandPermission permission) {
        super(method, permission);
    }

    @Override
    public boolean matches(CommandSender sender, String[] args) {
        return true;
    }

    @Override
    public MatchPriority compareMatch(DefaultCommandExecutor toCompare, CommandSender sender, String[] args) {
        return MatchPriority.THIS;
    }
}
