package me.noci.quickutilities.quickcommand.executor;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import me.noci.quickutilities.quickcommand.QuickCommand;
import me.noci.quickutilities.quickcommand.annotation.CommandPermission;
import me.noci.quickutilities.quickcommand.mappings.CommandMapping;
import me.noci.quickutilities.quickcommand.mappings.spacedvalues.SpacedValue;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BaseCommandExecutor implements CommandExecutor {

    protected final Method method;
    protected final CommandPermission permissionNode;
    protected final List<String> permissions;
    protected boolean fixedArgumentLength;
    protected int argumentLength;

    public BaseCommandExecutor(Method method, CommandPermission permissionNode) {
        this.method = method;
        this.method.setAccessible(true);

        this.permissionNode = permissionNode;

        List<String> permissions = Lists.newArrayList();
        if (this.permissionNode != null && this.permissionNode.value() != null) {
            permissions = Arrays.stream(this.permissionNode.value())
                    .filter(Objects::nonNull)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        this.permissions = permissions;

        boolean fixedArgumentLength = true;
        if (method.getParameterCount() > 1) {
            boolean spacedValue = SpacedValue.class.isAssignableFrom(method.getParameters()[method.getParameterCount() - 1].getType());
            fixedArgumentLength = !spacedValue;
        }

        this.fixedArgumentLength = fixedArgumentLength;
        this.argumentLength = method.getParameterCount() - 1;
    }

    @Override
    @SneakyThrows
    public void execute(QuickCommand command, CommandSender sender, String[] args) {
        Object[] params = CommandMapping.mapParameters(method, sender, args);
        method.invoke(command, params);
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        if (permissionNode == null) return true;
        Stream<String> permissions = this.permissions.stream();
        return permissionNode.strict() ? permissions.allMatch(sender::hasPermission) : permissions.anyMatch(sender::hasPermission);
    }

    @Override
    public Method method() {
        return method;
    }
}
