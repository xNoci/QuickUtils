package me.noci.quickutilities.utils.formatter;

import org.bukkit.entity.Player;

public interface FormatKey {

    FormatKey MESSAGE = new DefaultKey("%message%");
    FormatKey PLAYER_NAME = new DefaultKey("%player_name%") {
        @Override
        public String replace(String message, Object o) {
            if (o instanceof Player player) {
                return replace(message, player.getName());
            }
            return super.replace(message, o);
        }
    };

    String getKey();

    default String replace(String message, Object o) {
        return replace(message, o.toString());
    }

    default String replace(String message, String value) {
        return message.replace(getKey(), value);
    }

    class DefaultKey implements FormatKey {

        private final String key;

        public DefaultKey(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public String toString() {
            return this.key;
        }
    }

}
