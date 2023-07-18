package me.noci.quickutilities.quickcommand.executor;

import lombok.SneakyThrows;
import me.noci.quickutilities.quickcommand.QuickCommand;
import me.noci.quickutilities.quickcommand.mappings.CommandMapping;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;

public class FallbackCommandExecutor implements CommandExecutor {

    private final Method method;

    public FallbackCommandExecutor(Method method) {
        this.method = method;
        this.method.setAccessible(true);
    }

    @Override
    @SneakyThrows
    public void execute(QuickCommand command, CommandSender sender, String[] args) {
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
    public Method method() {
        return method;
    }

}
