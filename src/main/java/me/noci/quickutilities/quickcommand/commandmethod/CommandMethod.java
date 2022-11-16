package me.noci.quickutilities.quickcommand.commandmethod;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import me.noci.quickutilities.quickcommand.QuickCommand;
import me.noci.quickutilities.quickcommand.annotations.CommandPermission;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class CommandMethod<T extends CommandMethod<?>> {

    protected final Method method;
    protected final CommandPermission permissionNode;
    protected final int commandArgs;
    protected final boolean requiresPlayer;

    protected CommandMethod(Method method, CommandPermission permissionNode, int commandArgs, boolean requiresPlayer) {
        this.method = method;
        this.permissionNode = permissionNode;
        this.commandArgs = commandArgs;
        this.requiresPlayer = requiresPlayer;
    }

    @SneakyThrows
    public void execute(QuickCommand command, CommandSender sender, String[] args) {
        Object[] params = new Object[method.getParameterCount()];

        for (int i = 0; i < params.length; i++) {
            Parameter param = method.getParameters()[i];
            if (param.getType().equals(String[].class)) params[i] = args;
            if (CommandSender.class.isAssignableFrom(param.getType())) params[i] = sender;
        }

        method.setAccessible(true);
        method.invoke(command, params);
    }

    protected boolean hasFixedArgCount() {
        return commandArgs != -1;
    }

    public boolean hasPermission(CommandSender sender) {
        if (!needsPermission()) return true;
        Stream<String> permissions = filteredPermissions().stream();

        return permissionNode.strict() ? permissions.allMatch(sender::hasPermission) : permissions.anyMatch(sender::hasPermission);
    }

    public boolean needsPermission() {
        return permissionNode != null && filteredPermissions().size() > 0;
    }

    private List<String> filteredPermissions() {
        if (permissionNode.value() == null) return Lists.newArrayList();
        return Arrays.stream(permissionNode.value())
                .filter(Objects::nonNull)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public boolean matches(CommandSender sender, String[] args) {
        if (requiresPlayer && !(sender instanceof Player)) return false;
        return doesCommandMatch(sender, args);
    }

    public int findBestMatch(@NotNull T other, CommandSender sender, String[] args) {
        return calculatePriority(other, sender, args).priority;
    }

    protected abstract boolean doesCommandMatch(CommandSender sender, String[] args);

    protected abstract MatchPriority calculatePriority(T other, CommandSender sender, String[] args);

    protected enum MatchPriority {

        THIS(-1),
        OTHER(1),
        EQUAL(0);

        private final int priority;

        MatchPriority(int priority) {
            this.priority = priority;
        }

    }

}
