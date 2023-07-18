package me.noci.quickutilities.qcommand.executor;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import me.noci.quickutilities.qcommand.QCommand;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CommandExecutorFactory {

    @SuppressWarnings("unchecked")
    public static <T extends CommandExecutor<T>> List<T> loadExecutors(Class<? extends QCommand> commandClass, Class<? extends Annotation> type, Class<T> executorType) {
        Set<Method> methods = getClassMethods(commandClass);
        List<T> executors = Lists.newArrayList();

        for (Method method : methods) {
            if (!method.isAnnotationPresent(type)) continue;
            T executor = (T) create(method, executorType);
            if (executor == null) continue;
            executors.add(executor);
        }

        return executors;
    }

    private static <T extends CommandExecutor<T>> CommandExecutor<?> create(Method method, Class<T> executorType) {
        //TODO
        if (executorType.equals(FallbackCommandExecutor.class)) return new FallbackCommandExecutor();
        if (executorType.equals(SubCommandCommandExecutor.class)) return new SubCommandCommandExecutor();
        if (executorType.equals(CommandCommandExecutor.class)) return new CommandCommandExecutor();
        return null;
    }

    private static Set<Method> getClassMethods(Class<?> clazz) {
        Set<Method> methods = Sets.newLinkedHashSet();

        Collections.addAll(methods, clazz.getDeclaredMethods());
        Collections.addAll(methods, clazz.getMethods());

        return methods;
    }

}
