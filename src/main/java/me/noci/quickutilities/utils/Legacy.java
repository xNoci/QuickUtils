package me.noci.quickutilities.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class Legacy {

    public static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    public static String serialize(Component component) {
        return component == null ? null : SERIALIZER.serialize(component);
    }

    public static Component deserialize(@NotNull String text) {
        return SERIALIZER.deserialize(text);
    }

    private Legacy() {
    }

}
