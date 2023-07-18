package me.noci.quickutilities.qcommand.executor;

import lombok.SneakyThrows;
import me.noci.quickutilities.qcommand.QCommand;
import me.noci.quickutilities.qcommand.mappings.CommandMapping;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;

public class DefaultCommandExecutor implements CommandExecutor<DefaultCommandExecutor> {

    private final Method method;

    public DefaultCommandExecutor(Method method) {
        this.method = method;
        this.method.setAccessible(true);
    }

    @Override
    @SneakyThrows
    public void execute(QCommand command, CommandSender sender, String[] args) {
        Object[] params = CommandMapping.mapParameters(method, sender, args);
        method.invoke(command, params);
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return true;
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
