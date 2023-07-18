package me.noci.quickutilities.qcommand.mappings;

import com.google.common.collect.Maps;
import me.noci.quickutilities.utils.Require;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;

public class CommandMapping {

    private static final HashMap<Class<?>, PlayerMapping<?>> PLAYER_MAPPING = Maps.newHashMap();
    private static final HashMap<Class<?>, ArgumentMapping<?>> ARGUMENT_MAPPING = Maps.newHashMap();

    static {
        registerArgumentMapping(World.class, Bukkit::getWorld);
        registerArgumentMapping(Player.class, Bukkit::getPlayer);
        registerArgumentMapping(GameMode.class, argument -> switch (argument.toLowerCase()) {
            case "0", "survival" -> GameMode.SURVIVAL;
            case "1", "creative" -> GameMode.CREATIVE;
            case "2", "adventure" -> GameMode.ADVENTURE;
            case "3", "spec", "spectator" -> GameMode.SPECTATOR;
            default -> null;
        });
        registerArgumentMapping(String.class, argument -> argument);
        registerArgumentMapping(char.class, argument -> argument.charAt(0));
        registerArgumentMapping(char[].class, String::toCharArray);
        registerArgumentMapping(byte.class, Byte::parseByte);
        registerArgumentMapping(short.class, Short::parseShort);
        registerArgumentMapping(int.class, Integer::parseInt);
        registerArgumentMapping(long.class, Long::parseLong);
        registerArgumentMapping(boolean.class, Boolean::parseBoolean);
        registerArgumentMapping(float.class, Float::parseFloat);
        registerArgumentMapping(double.class, Double::parseDouble);
    }

    public static <T> void registerPlayerMapping(Class<T> mappingType, PlayerMapping<T> mapping) {
        Require.checkState(() -> !PLAYER_MAPPING.containsKey(mappingType), "Cannot register player mapping for type '%s' twice".formatted(mappingType.getName()));
        PLAYER_MAPPING.put(mappingType, mapping);
    }

    public static <T> void registerArgumentMapping(Class<T> argumentType, ArgumentMapping<T> mapping) {
        Require.checkState(() -> !ARGUMENT_MAPPING.containsKey(argumentType), "Cannot register argument mapping for type '%s' twice".formatted(argumentType.getName()));
        ARGUMENT_MAPPING.put(argumentType, mapping);
    }

    public static boolean isSenderType(Class<?> type) {
        if (type.equals(CommandSender.class) || type.equals(ConsoleCommandSender.class) || type.equals(Player.class))
            return true;
        return PLAYER_MAPPING.containsKey(type);
    }

    public static boolean isArgumentType(Class<?> type) {
        return ARGUMENT_MAPPING.containsKey(type);
    }

    public static Object[] mapParameters(Method method, CommandSender sender, String[] args) {
        Parameter[] methodParameter = method.getParameters();
        Object[] mappedParameters = new Object[method.getParameterCount()];

        mappedParameters[0] = mapSender(sender, methodParameter[0].getType());

        for (int i = 1; i < mappedParameters.length; i++) {
            boolean lastParameter = i == mappedParameters.length - 1;
            boolean isString = methodParameter[i].getType().equals(String.class);
            if (lastParameter && isString) {
                mappedParameters[i] = String.join(" ", Arrays.asList(args).subList(i - 1, args.length));
                break;
            }

            mappedParameters[i] = mapArgument(args[i - 1], methodParameter[i].getType());
        }

        return mappedParameters;
    }

    public static boolean matchesSenderType(Method method, CommandSender sender) {
        if(method.getParameterCount() == 0) return false;
        try {
            Object senderType = mapSender(sender, method.getParameters()[0].getType());
            return senderType != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Nullable
    private static <T> T mapSender(CommandSender sender, Class<T> mappingType) throws MappingException {
        if (mappingType.equals(Player.class) && sender instanceof Player) return (T) sender;
        if (sender instanceof Player player) {
            Require.check(() -> PLAYER_MAPPING.containsKey(mappingType), new MappingException("Could not find player mapping for type '%s'".formatted(mappingType.getName())));
            PlayerMapping<T> mapping = (PlayerMapping<T>) PLAYER_MAPPING.get(mappingType);
            return mapOrThrow(mapping, player, mappingType, Player.class);
        }

        if (mappingType.equals(CommandSender.class)) return (T) sender;
        if (mappingType.equals(ConsoleCommandSender.class) && sender instanceof ConsoleCommandSender) return (T) sender;
        throw new MappingException("Failed to map %s to '%s'".formatted(sender.getName(), mappingType.getName()));
    }

    @Nullable
    private static <T> T mapArgument(String argument, Class<T> mappingType) throws MappingException {
        if (mappingType.isEnum() && !ARGUMENT_MAPPING.containsKey(mappingType)) {
            for (T enumConstant : mappingType.getEnumConstants()) {
                if (enumConstant.toString().equalsIgnoreCase(argument)) return enumConstant;
            }
            return null;
        }

        Require.check(() -> ARGUMENT_MAPPING.containsKey(mappingType), new MappingException("Could not find argument mapping for type '%s'".formatted(mappingType.getName())));
        ArgumentMapping<T> mapping = (ArgumentMapping<T>) ARGUMENT_MAPPING.get(mappingType);
        return mapOrThrow(mapping, argument, mappingType, String.class);
    }

    private static <R, A> R mapOrThrow(Mapping<R, A> mapper, A toMap, Class<R> returnType, Class<A> mapType) throws MappingException {
        try {
            return mapper.map(toMap);
        } catch (Exception e) {
            throw new MappingException("Failed to map %s from '%s' to '%s'.".formatted(toMap.toString(), mapType.getName(), returnType.getName()), e);
        }
    }

}
