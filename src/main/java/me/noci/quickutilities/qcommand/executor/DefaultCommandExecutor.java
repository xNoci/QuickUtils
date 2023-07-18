package me.noci.quickutilities.qcommand.executor;

import me.noci.quickutilities.qcommand.annotation.CommandPermission;
import me.noci.quickutilities.qcommand.mappings.CommandMapping;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;

public class DefaultCommandExecutor extends BaseCommandExecutor<DefaultCommandExecutor> {

    public DefaultCommandExecutor(Method method, CommandPermission permission) {
        super(method, permission);
    }

    @Override
    public boolean matches(CommandSender sender, String[] args) {
        boolean matchesSenderType = CommandMapping.matchesSenderType(method, sender, true);
        if (!matchesSenderType) return false;
        if (fixedArgumentLength && args.length != argumentLength) return false;
        return CommandMapping.doesArgsMatchParameters(method, args);
    }

    @Override
    public MatchPriority compareMatch(DefaultCommandExecutor toCompare, CommandSender sender, String[] args) {
        return MatchPriority.THIS;
    }
}
