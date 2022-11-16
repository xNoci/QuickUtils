package me.noci.quickutilities.quickcommand.commandmethod;

import com.google.common.collect.ImmutableList;
import me.noci.quickutilities.quickcommand.annotations.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Stream;

public class CommandMethodFactory {

    private static final ImmutableList<Class<?>> VALID_PARAMETER_TYPES = ImmutableList.of(CommandSender.class, String[].class);

    @SuppressWarnings("rawtypes")
    public static CommandMethod createMethod(Method method) {
        validate(method);

        CommandPermission permission = CommandMethodFactory.getCommandPermission(method, null);
        String[] subcommandNames = CommandMethodFactory.parseSubcommandNames(method, null);
        int commandArgs = CommandMethodFactory.parseCommandArgs(method, -1);
        boolean requirePlayer = CommandMethodFactory.methodRequirePlayer(method);


        if (method.isAnnotationPresent(DefaultCommand.class)) return new DefaultCommandMethod(method, permission, commandArgs, requirePlayer);
        if (method.isAnnotationPresent(Subcommand.class)) return new SubcommandMethod(method, permission, subcommandNames, commandArgs, requirePlayer);
        if (method.isAnnotationPresent(UnknownCommand.class)) return new UnknownCommandMethod(method, permission, commandArgs, requirePlayer);

        return null;
    }

    private static void validate(Method method) {
        if (!method.isAnnotationPresent(DefaultCommand.class) && !method.isAnnotationPresent(Subcommand.class) && !method.isAnnotationPresent(UnknownCommand.class)) {
            throw new RuntimeException("Not a valid command method. A command annotation is required!");
        }

        if (method.getParameterCount() > 2) {
            throw new IllegalArgumentException("Command method cannot have more than 2 arguments.");
        }

        Stream<Class<?>> parameterTypes = Arrays.stream(method.getParameters()).map(Parameter::getType);

        //Check if one of the parameter type isn't valid
        if (parameterTypes
                .filter(type -> !VALID_PARAMETER_TYPES.contains(type))
                .anyMatch(type -> VALID_PARAMETER_TYPES.stream().noneMatch(validType -> validType.isAssignableFrom(type)))
        ) {
            throw new IllegalStateException("Command method can only contain parameters of type: %s and %s.".formatted(CommandSender.class, String[].class));
        }
    }

    private static int parseCommandArgs(Method method, int def) {
        if (!method.isAnnotationPresent(CommandArgs.class)) return def;
        return method.getDeclaredAnnotation(CommandArgs.class).value();
    }

    private static CommandPermission getCommandPermission(Method method, String def) {
        if (!method.isAnnotationPresent(CommandPermission.class)) return null;
        return method.getDeclaredAnnotation(CommandPermission.class);
    }

    private static String[] parseSubcommandNames(Method method, String[] def) {
        if (!method.isAnnotationPresent(Subcommand.class)) return def;
        return method.getDeclaredAnnotation(Subcommand.class).value().split(" ");
    }

    private static boolean methodRequirePlayer(Method method) {
        for (Parameter parameter : method.getParameters()) {
            if (!CommandSender.class.isAssignableFrom(parameter.getType())) continue;
            if (Player.class.isAssignableFrom(parameter.getType())) {
                return true;
            }
        }

        return false;
    }

}
