package me.noci.quickutilities.qcommand.executor;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.noci.quickutilities.qcommand.QCommand;
import me.noci.quickutilities.qcommand.annotation.CommandPermission;
import me.noci.quickutilities.qcommand.annotation.FallbackCommand;
import me.noci.quickutilities.qcommand.mappings.CommandMapping;
import me.noci.quickutilities.utils.Require;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CommandExecutorFactory {

    @SuppressWarnings("unchecked")
    public static <T extends CommandExecutor<T>> List<T> loadExecutors(Class<? extends QCommand> commandClass, Class<? extends Annotation> annotationType, Class<T> executorType) {
        Set<Method> methods = getClassMethods(commandClass);
        List<T> executors = Lists.newArrayList();

        for (Method method : methods) {
            try {
                T executor = (T) create(method, annotationType, executorType);
                if (executor == null) continue;
                executors.add(executor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return executors;
    }

    private static <T extends CommandExecutor<T>> CommandExecutor<?> create(Method method, Class<? extends Annotation> annotationType, Class<T> executorType) {
        if (!method.isAnnotationPresent(annotationType)) return null;

        Require.checkState(() -> method.getParameterCount() > 0, "A command method has to have at least one parameter");

        if (annotationType.equals(FallbackCommand.class) && method.getParameterCount() != 1) {
            throw new IllegalStateException("A fallback command method needs exactly one parameter");
        }

        Parameter[] parameters = method.getParameters();
        Require.checkState(() -> CommandMapping.isSenderType(parameters[0].getType()), "The first method parameter has to be a sender parameter");

        for (int i = 1; i < parameters.length; i++) {
            Parameter current = parameters[i];
            Require.checkState(() -> CommandMapping.isArgumentType(current.getType()), "The parameter '%s' at index %s in %s is not a valid command parameter type".formatted(current.getName(), i, method.getName()));
        }

        CommandPermission permissionNode = method.getDeclaredAnnotation(CommandPermission.class);

        if (executorType.equals(FallbackCommandExecutor.class)) return new FallbackCommandExecutor(method);
        if (executorType.equals(SubCommandCommandExecutor.class))
            return new SubCommandCommandExecutor(method, permissionNode);
        if (executorType.equals(DefaultCommandExecutor.class))
            return new DefaultCommandExecutor(method, permissionNode);
        return null;
    }

    private static Set<Method> getClassMethods(Class<?> clazz) {
        Set<Method> methods = Sets.newLinkedHashSet();

        Collections.addAll(methods, clazz.getDeclaredMethods());
        Collections.addAll(methods, clazz.getMethods());

        return methods;
    }

}
