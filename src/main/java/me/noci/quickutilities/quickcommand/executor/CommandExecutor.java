package me.noci.quickutilities.quickcommand.executor;

import me.noci.quickutilities.quickcommand.QuickCommand;
import me.noci.quickutilities.quickcommand.mappings.CommandMapping;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public interface CommandExecutor {

    void execute(QuickCommand command, CommandSender sender, String[] args);

    boolean hasPermission(CommandSender sender);

    boolean matches(CommandSender sender, String[] args);

    Method method();

    default MatchPriority compareMatch(CommandExecutor toCompare, CommandSender sender) {
        boolean thisMatches = CommandMapping.matchesSenderType(method(), sender, false);
        boolean otherMatches = CommandMapping.matchesSenderType(toCompare.method(), sender, false);

        if (thisMatches && otherMatches) return MatchPriority.EQUAL;
        if (otherMatches) return MatchPriority.OTHER;
        return MatchPriority.THIS;
    }

    static Optional<CommandExecutor> bestMatch(List<CommandExecutor> commands, CommandSender sender, String[] args) {
        return commands.stream().filter(command -> command.matches(sender, args))
                .min((o1, o2) -> o1.compareMatch(o2, sender).priority);
    }

    enum MatchPriority {
        THIS(-1), OTHER(1), EQUAL(0);

        private final int priority;

        MatchPriority(int priority) {
            this.priority = priority;
        }
    }

}
