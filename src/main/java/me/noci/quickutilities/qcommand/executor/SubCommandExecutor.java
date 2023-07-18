package me.noci.quickutilities.qcommand.executor;

import me.noci.quickutilities.qcommand.QCommand;
import me.noci.quickutilities.qcommand.annotation.CommandPermission;
import me.noci.quickutilities.qcommand.annotation.SubCommand;
import me.noci.quickutilities.qcommand.mappings.CommandMapping;
import me.noci.quickutilities.utils.Require;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class SubCommandExecutor extends BaseCommandExecutor<SubCommandExecutor> {

    private String[] subCommandPath;

    public SubCommandExecutor(Method method, CommandPermission permission, SubCommand subCommand) {
        super(method, permission);

        if (subCommand != null && subCommand.path() != null) {
            subCommandPath = Arrays.stream(subCommand.path())
                    .filter(Objects::nonNull)
                    .filter(StringUtils::isNotBlank)
                    .toArray(String[]::new);
        }

        Require.checkState(() -> subCommandPath != null && subCommandPath.length > 0, "");
    }

    @Override
    public void execute(QCommand command, CommandSender sender, String[] args) {
        super.execute(command, sender, Arrays.copyOfRange(args, subCommandPath.length, args.length));
    }

    @Override
    public boolean matches(CommandSender sender, String[] args) {
        if (args.length < subCommandPath.length) return false;

        boolean matchesSenderType = CommandMapping.matchesSenderType(method, sender, true);
        if (!matchesSenderType) return false;

        for (int i = 0; i < subCommandPath.length; i++) {
            if (!args[i].equalsIgnoreCase(subCommandPath[i])) return false;
        }

        args = Arrays.copyOfRange(args, subCommandPath.length, args.length);

        if (fixedArgumentLength && args.length != argumentLength) return false;
        return CommandMapping.doesArgsMatchParameters(method, args);
    }

    @Override
    public MatchPriority compareMatch(SubCommandExecutor toCompare, CommandSender sender, String[] args) {

        return MatchPriority.THIS;
    }

}
