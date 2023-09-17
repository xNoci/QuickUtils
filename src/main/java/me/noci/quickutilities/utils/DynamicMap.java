package me.noci.quickutilities.utils;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

public class DynamicMap<K> {

    private final Map<K, Value<?>> DATA = Maps.newHashMap();

    public static <T> DynamicMap<T> create() {
        return new DynamicMap<>();
    }

    @SuppressWarnings("unchecked")
    public <T> void set(K key, T value) {
        Value<T> cache = (Value<T>) getCached(key, value.getClass());
        if (cache.getValue() != null && !cache.getValue().getClass().equals(value.getClass()))
            throw new UnsupportedOperationException(String.format("Used wrong type for %s. Used type '%s' requires type '%s'", key, value.getClass(), cache.getValue().getClass()));
        cache.setValue(value);
    }

    public Object get(K key) {
        return getCached(key, Object.class).getValue();
    }

    public <T> T get(K key, Class<T> type) {
        return getCached(key, type).getValue();
    }

    @SuppressWarnings("unchecked")
    private <T> Value<T> getCached(K key, Class<T> type) {
        if (DATA.containsKey(key)) return (Value<T>) DATA.get(key);
        Value<T> value = new Value<>();
        value.setType(type);
        DATA.put(key, value);
        return value;
    }

    public Class<?> getType(K key) {
        if (!DATA.containsKey(key)) return null;
        Value<?> value = DATA.get(key);
        return value.getType();
    }

    public Set<K> getKeys() {
        return DATA.keySet();
    }

    private static class Value<T> {
        @Getter @Setter private T value = null;
        @Getter @Setter private Class<T> type;
    }

}