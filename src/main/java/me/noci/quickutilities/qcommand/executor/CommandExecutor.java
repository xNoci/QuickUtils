package me.noci.quickutilities.qcommand.executor;

import me.noci.quickutilities.qcommand.QCommand;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;

public interface CommandExecutor<T extends CommandExecutor<T>> {

    void execute(QCommand command, CommandSender sender, String[] args);

    boolean hasPermission(CommandSender sender);

    boolean matches(CommandSender sender, String[] args);

    MatchPriority compareMatch(T toCompare, CommandSender sender, String[] args);

    static <T extends CommandExecutor<T>> Optional<T> bestMatch(List<T> commands, CommandSender sender, String[] args) {
        return commands.stream().filter(command -> command.matches(sender, args))
                .min((o1, o2) -> o1.compareMatch(o2, sender, args).priority);
    }

    enum MatchPriority {
        THIS(-1), OTHER(1), EQUAL(0);

        private final int priority;

        MatchPriority(int priority) {
            this.priority = priority;
        }
    }

}
