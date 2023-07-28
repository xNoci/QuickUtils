package me.noci.quickutilities.utils;

import com.cryptomorin.xseries.ReflectionUtils;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

public class EntityCountAccessor {

    private static final Field ENTITY_ID_COUNT;
    private static final boolean IS_ATOMIC_INTEGER = ReflectionUtils.supports(14);

    static {
        //8 - entityCount : int
        //9 - entityCount : int
        //10 - entityCount : int
        //11 - entityCount : int
        //12 - entityCount : int
        //13 - entityCount : int
        //14 - entityCount : AtomicInteger
        //15 - entityCount : AtomicInteger
        //16 - entityCount : AtomicInteger
        //17 - b : AtomicInteger
        //18 - c : AtomicInteger
        //19 - d : AtomicInteger
        //20 - d : AtomicInteger

        String fieldName = ReflectionUtils.v(19, "d").v(18, "c").v(17, "b").orElse("entityCount");
        Class<?> entityClass = ReflectionUtils.getNMSClass("world.entity", "Entity");

        Field field = null;
        try {
            if (entityClass != null) {
                field = entityClass.getDeclaredField(fieldName);
            }
        } catch (NoSuchFieldException ignore) {
        }

        ENTITY_ID_COUNT = field;
        if (ENTITY_ID_COUNT != null) {
            ENTITY_ID_COUNT.setAccessible(true);
        }
    }

    @SneakyThrows
    protected static int get() {
        Require.nonNull(ENTITY_ID_COUNT, "This is currently not supported in your version");
        if (IS_ATOMIC_INTEGER) return ((AtomicInteger) ENTITY_ID_COUNT.get(null)).getAndIncrement();
        int id = (int) ENTITY_ID_COUNT.get(null);
        id++;
        ENTITY_ID_COUNT.set(null, id);
        return id;
    }

}
