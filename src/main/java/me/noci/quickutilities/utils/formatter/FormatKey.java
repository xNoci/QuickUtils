package me.noci.quickutilities.utils.formatter;

import org.bukkit.entity.Player;

public interface FormatKey {

    FormatKey MESSAGE = FormatKey.of(String.class, "%message%", s -> s);
    FormatKey PLAYER_NAME = FormatKey.of(Player.class, "%player_name%", Player::getName);

    String getKey();

    default String replace(String message, Object o) {
        return replace(message, o.toString());
    }

    default String replace(String message, String value) {
        return message.replace(getKey(), value);
    }

    static <T> FormatKey of(Class<T> type, String key, FormatKeyConverter<T> converter) {
        return new FormatKeyImpl<>(type, key, converter);
    }

    class FormatKeyImpl<T> implements FormatKey {

        private final Class<T> type;
        private final String key;
        private final FormatKeyConverter<T> converter;

        private FormatKeyImpl(Class<T> type, String key, FormatKeyConverter<T> converter) {
            this.type = type;
            this.key = key;
            this.converter = converter;
        }

        @Override
        public String replace(String message, Object o) {
            if (type.isInstance(o)) {
                return converter.convert(cast(o));
            }
            return FormatKey.super.replace(message, o);
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public String toString() {
            return this.key;
        }

        private T cast(Object o) {
            return (T) o;
        }

    }

}
