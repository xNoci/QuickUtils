package me.noci.quickutilities.quickcommand;

import com.google.common.collect.ObjectArrays;
import lombok.Getter;
import me.noci.quickutilities.quickcommand.annotation.Command;
import me.noci.quickutilities.quickcommand.annotation.FallbackCommand;
import me.noci.quickutilities.quickcommand.annotation.SubCommand;
import me.noci.quickutilities.quickcommand.executor.*;
import me.noci.quickutilities.utils.Require;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public abstract class QuickCommand {

    private final List<CommandExecutor> fallbackCommandList;
    private final List<CommandExecutor> subCommandList;
    private final List<CommandExecutor> defaultCommandList;

    @Getter private final JavaPlugin plugin;
    @Getter private final String name;
    @Getter private final List<String> aliases;
    @Getter private final String description;
    @Getter private final String usage;

    protected String noPermission = "§cI'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.";
    protected String noFallbackCommand = "§cCould not find a fallback command for '/%s' please report this issue to a developer.";

    protected QuickCommand(JavaPlugin plugin, String name, String... aliases) {
        this(plugin, name, List.of(aliases), "", "");
    }

    protected QuickCommand(JavaPlugin plugin, String name, List<String> aliases, String description, String usage) {
        Require.nonNull(plugin, "Plugin cannot be null");
        Require.nonNull(name, "Name cannot be null");
        Require.checkState(!StringUtils.isBlank(name), "Name cannot be empty or blank");
        Require.nonNull(aliases, "Aliases cannot be null");
        Require.nonNull(description, "Description cannot be null");
        Require.nonNull(usage, "Usage cannot be null");

        this.plugin = plugin;
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.usage = usage;

        fallbackCommandList = CommandExecutorFactory.loadExecutors(getClass(), FallbackCommand.class, FallbackCommandExecutor.class);
        subCommandList = CommandExecutorFactory.loadExecutors(getClass(), SubCommand.class, SubCommandExecutor.class);
        defaultCommandList = CommandExecutorFactory.loadExecutors(getClass(), Command.class, DefaultCommandExecutor.class);

        if (defaultCommandList.size() == 0) {
            plugin.getLogger().warning("Did not found any commands for '%s'".formatted(getClass().getName()));
        }

        if (fallbackCommandList.size() == 0) {
            plugin.getLogger().warning("Did not found any fallback commands for '%s'".formatted(getClass().getName()));
        }
    }

    protected void autoRegister() {
        CommandRegister.register(this);
    }

    protected boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        CommandExecutor executor = findCommand(sender, args);
        if (executor == null) {
            sender.sendMessage(noFallbackCommand.formatted(String.join(" ", ObjectArrays.concat(label, args))));
            return false;
        }

        if (!executor.hasPermission(sender)) {
            sender.sendMessage(noPermission);
            return true;
        }
        executor.execute(this, sender, args);
        return true;
    }

    private CommandExecutor findCommand(CommandSender sender, String[] args) {
        Optional<CommandExecutor> commands = CommandExecutor.bestMatch(subCommandList, sender, args);
        if(commands.isPresent()) return commands.get();

        commands = CommandExecutor.bestMatch(defaultCommandList, sender, args);
        if(commands.isPresent()) return commands.get();

        commands = CommandExecutor.bestMatch(fallbackCommandList, sender, args);
        return commands.orElse(null);
    }

}
