package me.noci.quickutilities.qcommand.executor;

import lombok.SneakyThrows;
import me.noci.quickutilities.qcommand.QCommand;
import me.noci.quickutilities.qcommand.mappings.CommandMapping;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;

public class SubCommandCommandExecutor implements CommandExecutor<SubCommandCommandExecutor> {

    private final Method method;

    public SubCommandCommandExecutor(Method method) {
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
        return false;
    }

    @Override
    public boolean matches(CommandSender sender, String[] args) {
        return false;
    }

    @Override
    public MatchPriority compareMatch(SubCommandCommandExecutor toCompare, CommandSender sender, String[] args) {
        return null;
    }
}
