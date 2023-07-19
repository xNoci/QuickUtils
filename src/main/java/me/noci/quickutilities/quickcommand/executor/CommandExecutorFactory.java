package me.noci.quickutilities.quickcommand.executor;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.noci.quickutilities.quickcommand.QuickCommand;
import me.noci.quickutilities.quickcommand.annotation.CommandPermission;
import me.noci.quickutilities.quickcommand.annotation.FallbackCommand;
import me.noci.quickutilities.quickcommand.annotation.SubCommand;
import me.noci.quickutilities.quickcommand.mappings.CommandMapping;
import me.noci.quickutilities.quickcommand.mappings.spacedvalues.SpacedValue;
import me.noci.quickutilities.utils.Require;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CommandExecutorFactory {

    public static List<CommandExecutor> loadExecutors(Class<? extends QuickCommand> commandClass, Class<? extends Annotation> annotationType, Class<?> executorType) {
        Set<Method> methods = getClassMethods(commandClass);
        List<CommandExecutor> executors = Lists.newArrayList();

        for (Method method : methods) {
            try {
                CommandExecutor executor = create(method, annotationType, executorType);
                if (executor == null) continue;
                executors.add(executor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return executors;
    }

    private static CommandExecutor create(Method method, Class<? extends Annotation> annotationType, Class<?> executorType) {
        if (!method.isAnnotationPresent(annotationType)) return null;
        Parameter[] parameters = method.getParameters();

        Require.checkState(method.getParameterCount() > 0, "A command method has to have at least one parameter. Method: %s#%s".formatted(method.getDeclaringClass().getName(), method.getName()));
        Require.checkState(!annotationType.equals(FallbackCommand.class) || method.getParameterCount() == 1, "A fallback command method needs exactly one parameter. Method: %s#%s".formatted(method.getDeclaringClass().getName(), method.getName()));
        Require.checkState(CommandMapping.isSenderType(parameters[0].getType()), "The first method parameter of %s#%s has to be a sender parameter".formatted(method.getDeclaringClass().getName(), method.getName()));

        for (int i = 1; i < parameters.length; i++) {
            Parameter current = parameters[i];
            boolean lastParameter = i == parameters.length - 1;

            Require.checkState(!SpacedValue.class.isAssignableFrom(current.getType()) || lastParameter, "Spaced value parameter '%s (%s)' at index %s in %s#%s is only allowed at last parameter position".formatted(current.getName(), current.getType().getName(), i, method.getDeclaringClass().getName(), method.getName()));
            Require.checkState(CommandMapping.isArgumentType(current.getType()), "The parameter '%s (%s)' at index %s in %s#%s is not a valid command parameter type".formatted(current.getName(), current.getType().getName(), i, method.getDeclaringClass().getName(), method.getName()));
        }

        CommandPermission permissionNode = method.getDeclaredAnnotation(CommandPermission.class);
        SubCommand subCommandNode = method.getDeclaredAnnotation(SubCommand.class);

        if (executorType.equals(FallbackCommandExecutor.class)) return new FallbackCommandExecutor(method);
        if (executorType.equals(SubCommandExecutor.class))
            return new SubCommandExecutor(method, permissionNode, subCommandNode);
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
