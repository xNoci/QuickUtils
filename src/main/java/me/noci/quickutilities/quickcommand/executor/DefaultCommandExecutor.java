package me.noci.quickutilities.quickcommand.executor;

import me.noci.quickutilities.quickcommand.annotation.CommandPermission;
import me.noci.quickutilities.quickcommand.mappings.CommandMapping;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;

public class DefaultCommandExecutor extends BaseCommandExecutor {

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

}
