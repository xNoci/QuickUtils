package me.noci.quickutilities.qcommand.executor;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import me.noci.quickutilities.qcommand.QCommand;
import me.noci.quickutilities.qcommand.mappings.CommandMapping;
import me.noci.quickutilities.quickcommand.annotations.CommandPermission;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BaseCommandExecutor<T extends CommandExecutor<T>> implements CommandExecutor<T> {

    private final Method method;
    private final CommandPermission permissionNode;
    private final List<String> permissions;

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
    }

    @Override
    @SneakyThrows
    public void execute(QCommand command, CommandSender sender, String[] args) {
        Object[] params = CommandMapping.mapParameters(method, sender, args);
        method.invoke(command, params);
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        if (permissionNode == null) return true;
        Stream<String> permissions = this.permissions.stream();
        return permissionNode.strict() ? permissions.allMatch(sender::hasPermission) : permissions.anyMatch(sender::hasPermission);
    }

}
