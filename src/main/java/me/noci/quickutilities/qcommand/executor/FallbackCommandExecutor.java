package me.noci.quickutilities.qcommand.executor;

import lombok.SneakyThrows;
import me.noci.quickutilities.qcommand.QCommand;
import me.noci.quickutilities.qcommand.mappings.CommandMapping;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;

public class FallbackCommandExecutor implements CommandExecutor<FallbackCommandExecutor> {

    private final Method method;

    public FallbackCommandExecutor(Method method) {
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
    public MatchPriority compareMatch(FallbackCommandExecutor toCompare, CommandSender sender, String[] args) {
        boolean thisMatches = CommandMapping.matchesSenderType(method, sender, false);
        boolean otherMatches = CommandMapping.matchesSenderType(toCompare.method, sender, false);

        if (thisMatches && otherMatches) return MatchPriority.EQUAL;
        if (otherMatches) return MatchPriority.OTHER;
        return MatchPriority.THIS;
    }
}
