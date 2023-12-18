package me.noci.quickutilities.utils;

import com.google.common.reflect.TypeToken;

@SuppressWarnings("UnstableApiUsage")
public abstract class GenericType<T> {
    protected final TypeToken<T> type = new TypeToken<>(getClass()) {
    };
}
