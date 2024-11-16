package me.noci.quickutilities.utils;

import com.cryptomorin.xseries.reflection.XReflection;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

public class EntityCountAccessor {

    private static final Field ENTITY_ID_COUNT;
    private static final boolean IS_ATOMIC_INTEGER = XReflection.supports(14);

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


        Field field = null;
        try {
            Class<?> entityClass = XReflection.ofMinecraft().inPackage("net.minecraft.world.entity").named("Entity").reflect();
            String fieldName = XReflection.v(20, 5, "ENTITY_COUNTER").v(19, "d").v(18, "c").v(17, "b").orElse("entityCount");
            field = entityClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
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
