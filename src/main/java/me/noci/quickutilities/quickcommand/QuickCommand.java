package me.noci.quickutilities.quickcommand;

import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.noci.quickutilities.quickcommand.annotations.DefaultCommand;
import me.noci.quickutilities.quickcommand.annotations.Subcommand;
import me.noci.quickutilities.quickcommand.annotations.UnknownCommand;
import me.noci.quickutilities.quickcommand.commandmethod.*;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class QuickCommand {

    @Getter private final JavaPlugin plugin;
    @Getter private final String name;
    @Getter private final List<String> aliases = Lists.newArrayList();
    private final List<DefaultCommandMethod> defaultCommands = Lists.newArrayList();
    private final List<SubcommandMethod> subcommands = Lists.newArrayList();
    private final List<UnknownCommandMethod> unknownCommands = Lists.newArrayList();
    @Getter @Setter(AccessLevel.PROTECTED) private String description = "";
    @Getter @Setter(AccessLevel.PROTECTED) private String usage = "";

    public QuickCommand(JavaPlugin plugin, String name, String... aliases) {
        this.plugin = plugin;
        this.name = name;
        this.aliases.addAll(List.of(aliases));

        loadCommandMethods();
    }

    protected void autoRegister() {
        CommandManager.register(this);
    }

    protected String noPermission() {
        return "§cI'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.";
    }

    protected String commandNotFound(String commandName, String[] args) {
        return "§cCould not find a fallback command for /%s please report this issue to a developer.".formatted(String.join(" ", ObjectArrays.concat(commandName, args)));
    }

    protected boolean execute(CommandSender sender, String commandName, String[] args) {
        CommandMethod<?> command = matchCommand(sender, args);

        if (command != null) {
            if (!command.hasPermission(sender)) {
                sender.sendMessage(noPermission());
                return true;
            }
            command.execute(this, sender, args);
            return true;
        }

        sender.sendMessage(commandNotFound(commandName, args));
        return false;
    }

    private CommandMethod<?> matchCommand(CommandSender sender, String[] args) {
        Optional<SubcommandMethod> subcommand = bestMatch(subcommands, sender, args);
        if (subcommand.isPresent()) return subcommand.get();

        Optional<DefaultCommandMethod> defaultCommand = bestMatch(defaultCommands, sender, args);
        if (defaultCommand.isPresent()) return defaultCommand.get();

        Optional<UnknownCommandMethod> unknownCommandMethod = bestMatch(unknownCommands, sender, args);
        return unknownCommandMethod.orElse(null);
    }

    private <T extends CommandMethod<T>> Optional<T> bestMatch(List<T> commands, CommandSender sender, String[] args) {
        return commands.stream().filter(command -> command.matches(sender, args))
                .min((o1, o2) -> o1.findBestMatch(o2, sender, args));
    }

    private void loadCommandMethods() {
        loadCommands(DefaultCommand.class, defaultCommands);
        loadCommands(Subcommand.class, subcommands);
        loadCommands(UnknownCommand.class, unknownCommands);

        if (defaultCommands.size() == 0) {
            plugin.getLogger().warning("No 'DefaultCommand' method found for %s".formatted(getClass().getName()));
        }

        if (unknownCommands.size() == 0) {
            plugin.getLogger().warning("No 'UnknownCommand' method found for %s".formatted(getClass().getName()));
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private <T extends CommandMethod<T>> void loadCommands(Class<? extends Annotation> type, List<T> commands) {
        Set<Method> methods = getClassMethods();

        for (Method method : methods) {
            if (!method.isAnnotationPresent(type)) continue;
            try {
                CommandMethod commandMethod = CommandMethodFactory.createMethod(method);
                if (commandMethod == null) continue;
                commands.add((T) commandMethod);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private Set<Method> getClassMethods() {
        Set<Method> methods = Sets.newLinkedHashSet();

        Collections.addAll(methods, getClass().getDeclaredMethods());
        Collections.addAll(methods, getClass().getMethods());

        return methods;
    }

}
