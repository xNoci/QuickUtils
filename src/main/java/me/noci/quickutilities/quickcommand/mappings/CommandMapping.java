package me.noci.quickutilities.quickcommand.mappings;

import com.google.common.collect.Maps;
import com.google.common.primitives.Primitives;
import me.noci.quickutilities.quickcommand.annotation.IgnoreStrictEnum;
import me.noci.quickutilities.quickcommand.mappings.spacedvalues.SpacedCharArray;
import me.noci.quickutilities.quickcommand.mappings.spacedvalues.SpacedString;
import me.noci.quickutilities.quickcommand.mappings.spacedvalues.SpacedValue;
import me.noci.quickutilities.utils.Fraction;
import me.noci.quickutilities.utils.Require;
import org.apache.commons.lang.ArrayUtils;
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
    private static final HashMap<Class<? extends SpacedValue<?>>, SpacedArgumentMapping<?>> SPACED_MAPPINGS = Maps.newHashMap();

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

        registerSpacedArgumentMapping(SpacedString.class, SpacedString::new);
        registerSpacedArgumentMapping(SpacedCharArray.class, SpacedCharArray::new);

        registerArgumentMapping(Fraction.class, Fraction::of);
        registerArgumentMapping(String.class, argument -> argument);
        registerArgumentMapping(Character[].class, argument -> ArrayUtils.toObject(argument.toCharArray()));
        registerArgumentMapping(char[].class, String::toCharArray);

        registerPrimitiveArgumentMapping(char.class, argument -> argument.charAt(0));
        registerPrimitiveArgumentMapping(byte.class, Byte::parseByte);
        registerPrimitiveArgumentMapping(short.class, Short::parseShort);
        registerPrimitiveArgumentMapping(int.class, Integer::parseInt);
        registerPrimitiveArgumentMapping(long.class, Long::parseLong);
        registerPrimitiveArgumentMapping(boolean.class, Boolean::parseBoolean);
        registerPrimitiveArgumentMapping(float.class, Float::parseFloat);
        registerPrimitiveArgumentMapping(double.class, Double::parseDouble);
    }

    public static <T> void registerPlayerMapping(Class<T> mappingType, PlayerMapping<T> mapping) {
        Require.checkArgument(!mappingType.equals(Player.class), "Cannot register player mapping for type '%s'".formatted(mappingType.getName()));
        Require.checkArgument(!mappingType.equals(CommandSender.class), "Cannot register player mapping for type '%s'".formatted(mappingType.getName()));
        Require.checkState(!PLAYER_MAPPING.containsKey(mappingType), "Cannot register player mapping for type '%s' twice".formatted(mappingType.getName()));
        PLAYER_MAPPING.put(mappingType, mapping);
    }

    public static <T extends SpacedValue<?>> void registerSpacedArgumentMapping(Class<T> argumentType, SpacedArgumentMapping<T> mapping) {
        Require.checkState(!SPACED_MAPPINGS.containsKey(argumentType), "Cannot register spaced argument mapping for type '%s' twice".formatted(argumentType.getName()));
        SPACED_MAPPINGS.put(argumentType, mapping);
    }

    public static <T> void registerArgumentMapping(Class<T> argumentType, ArgumentMapping<T> mapping) {
        Require.checkState(!SpacedValue.class.isAssignableFrom(argumentType), "Cannot register type '%s'. To register spaced values use registerSpacedArgumentMapping.".formatted(argumentType.getName()));
        Require.checkState(!ARGUMENT_MAPPING.containsKey(argumentType), "Cannot register argument mapping for type '%s' twice".formatted(argumentType.getName()));
        ARGUMENT_MAPPING.put(argumentType, mapping);
    }

    private static <T> void registerPrimitiveArgumentMapping(Class<T> argumentType, ArgumentMapping<T> mapping) {
        Require.checkArgument(argumentType.isPrimitive(), "This method only supports registering primitives");
        registerArgumentMapping(Primitives.unwrap(argumentType), mapping);
        registerArgumentMapping(Primitives.wrap(argumentType), mapping);
    }

    public static boolean isSenderType(Class<?> type) {
        if (type.equals(CommandSender.class) || type.equals(ConsoleCommandSender.class) || type.equals(Player.class))
            return true;
        return PLAYER_MAPPING.containsKey(type);
    }

    public static boolean isArgumentType(Class<?> type) {
        return ARGUMENT_MAPPING.containsKey(type) || SPACED_MAPPINGS.containsKey(type) || type.isEnum();
    }

    public static Object[] mapParameters(Method method, CommandSender sender, String[] args) {
        Parameter[] methodParameter = method.getParameters();
        Object[] mappedParameters = new Object[method.getParameterCount()];

        mappedParameters[0] = mapSender(sender, methodParameter[0].getType(), true);

        for (int i = 1; i < mappedParameters.length; i++) {
            boolean lastParameter = i == mappedParameters.length - 1;
            boolean isSpacedValue = SpacedValue.class.isAssignableFrom(methodParameter[i].getType());
            if (lastParameter && isSpacedValue) {
                String[] remainingArgs = Arrays.asList(args).subList(i - 1, args.length).toArray(String[]::new);
                mappedParameters[i] = mapSpacedArgument(remainingArgs, (Class<? extends SpacedValue<?>>) methodParameter[i].getType());
                break;
            }

            mappedParameters[i] = mapArgument(args[i - 1], methodParameter[i].getType());
        }

        return mappedParameters;
    }

    public static boolean doesArgsMatchParameters(Method method, String[] args) {
        Parameter[] methodParameter = method.getParameters();
        for (int i = 1; i < methodParameter.length; i++) {
            try {
                int argumentIndex = i - 1;
                if (args.length <= argumentIndex) return false;
                String currentArg = args[i - 1];
                Class<?> parameterType = methodParameter[i].getType();

                if (parameterType.isEnum() && methodParameter[i].isAnnotationPresent(IgnoreStrictEnum.class)) continue;

                if (SpacedValue.class.isAssignableFrom(parameterType)) {
                    Object spacedMapping = mapSpacedArgument(new String[]{currentArg}, (Class<? extends SpacedValue>) parameterType);
                    if (spacedMapping == null) return false;
                    continue;
                }

                Object mapping = mapArgument(currentArg, parameterType);
                if (mapping == null) return false;
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }

    public static boolean matchesSenderType(Method method, CommandSender sender, boolean matchPlayerToCommandSender) {
        if (method.getParameterCount() == 0) return false;
        try {
            Object senderType = mapSender(sender, method.getParameters()[0].getType(), matchPlayerToCommandSender);
            return senderType != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Nullable
    private static <T> T mapSender(CommandSender sender, Class<T> mappingType, boolean playerFallback) throws MappingException {
        if (mappingType.equals(Player.class) && sender instanceof Player) return (T) sender;
        if (sender instanceof Player player) {
            if (playerFallback && mappingType.equals(CommandSender.class)) {
                return (T) player;
            }

            Require.check(PLAYER_MAPPING.containsKey(mappingType), new MappingException("Could not find player mapping for type '%s'".formatted(mappingType.getName())));
            PlayerMapping<T> mapping = (PlayerMapping<T>) PLAYER_MAPPING.get(mappingType);
            return mapOrThrow(mapping, player, mappingType, Player.class);
        }

        if (mappingType.equals(CommandSender.class)) return (T) sender;
        if (mappingType.equals(ConsoleCommandSender.class) && sender instanceof ConsoleCommandSender) return (T) sender;
        throw new MappingException("Failed to map %s to '%s'".formatted(sender.getName(), mappingType.getName()));
    }

    @Nullable
    private static <T extends SpacedValue<?>> T mapSpacedArgument(String[] arguments, Class<T> mappingType) throws MappingException {
        Require.check(SPACED_MAPPINGS.containsKey(mappingType), new MappingException("Could not find spaced argument mapping for type '%s'".formatted(mappingType.getName())));
        SpacedArgumentMapping<T> mapping = (SpacedArgumentMapping<T>) SPACED_MAPPINGS.get(mappingType);
        return mapOrThrow(mapping, arguments, mappingType, String[].class);
    }

    @Nullable
    private static <T> T mapArgument(String argument, Class<T> mappingType) throws MappingException {
        if (mappingType.isEnum() && !ARGUMENT_MAPPING.containsKey(mappingType)) {
            for (T enumConstant : mappingType.getEnumConstants()) {
                if (enumConstant.toString().equalsIgnoreCase(argument)) return enumConstant;
            }
            return null;
        }

        Require.check(ARGUMENT_MAPPING.containsKey(mappingType), new MappingException("Could not find argument mapping for type '%s'".formatted(mappingType.getName())));
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
