package me.noci.quickutilities.qcommand;

import lombok.SneakyThrows;
import me.noci.quickutilities.utils.Require;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class CommandRegister {

    private static final MethodHandle COMMAND_MAP;

    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle commandMap = null;

        try {
            commandMap = lookup.findVirtual(Bukkit.getServer().getClass(), "getCommandMap", MethodType.methodType(SimpleCommandMap.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

        COMMAND_MAP = commandMap;
    }

    @SneakyThrows
    public static void register(QCommand command) {
        Require.checkArgument(() -> command.getPlugin() != null, "Plugin cannot be null");
        Require.checkArgument(() -> command.getName() != null && !StringUtils.isBlank(command.getName()), "Command name cannot be null or empty");

        SimpleCommandMap commandMap = (SimpleCommandMap) COMMAND_MAP.invoke(Bukkit.getServer());
        commandMap.register(command.getPlugin().getName(), new CommandRegister.CommandContainer(command));
    }


    private static class CommandContainer extends org.bukkit.command.Command implements PluginIdentifiableCommand {
        private final QCommand command;

        public CommandContainer(QCommand command) {
            super(command.getName(), command.getDescription(), command.getUsage(), command.getAliases());
            this.command = command;
        }

        @Override
        public boolean execute(@NotNull CommandSender commandSender, @NotNull String commandLabel, String[] args) {
            return command.execute(commandSender, commandLabel, args);
        }

        @Override
        public Plugin getPlugin() {
            return this.command.getPlugin();
        }
    }

}
