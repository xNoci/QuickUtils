package me.noci.quickutilities.utils.formatter;

import me.noci.quickutilities.utils.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class ExampleFormatKey {

    // Creating static format keys
    private static final FormatKey STATIC_FORMAT_KEY = FormatKey.of(Player.class, "%player_health%", player -> Double.toString(player.getHealth()));

    private ExampleFormatKey() {
    }

    private static void exampleUse() {
        // You can use the keys in messages either by using KEY.getKey()
        // or if the class/enum implementation of FormatKey overrides toString()
        // to return the key instead. You can just use the enum/variable.
        String exampleMessage = "The favourite color of " + FormatKey.PLAYER_NAME +
                "is " + ExampleEnumFormatKey.COLOR.getKey() +
                " his favourite letter is " + ExampleEnumDefaultFormatKey.TEXT + " and his favourite word is " + FormatKey.MESSAGE.getKey() + "." +
                " He  currently has " + STATIC_FORMAT_KEY + " lives.";

        for (Player player : Bukkit.getOnlinePlayers()) {
            ChatColor favColor = RandomUtils.randomChatColor();

            MessageFormatter formatter = MessageFormatter.format(exampleMessage);    // Creating Message Formatter
            formatter.apply(ExampleEnumFormatKey.COLOR, favColor);                   // Key via EnumFormatKey
            formatter.apply(ExampleEnumDefaultFormatKey.TEXT, "B");                  // Key via EnumDefaultFormatKey
            formatter.apply(FormatKey.PLAYER_NAME, player);                          // Default key
            formatter.apply(FormatKey.MESSAGE, "Tree");                              // Default key
            formatter.apply(STATIC_FORMAT_KEY, player);                              // Static created format key

            Bukkit.broadcastMessage(formatter.toString());
        }
    }

    // Creating format key with enum (using DefaultFormatKey)
    private enum ExampleEnumDefaultFormatKey implements DefaultFormatKey {
        TEXT(String.class, s -> s),
        COLOR(ChatColor.class, Enum::name);

        private final FormatKey formatKey;

        <T> ExampleEnumDefaultFormatKey(Class<T> type, FormatKeyConverter<T> converter) {
            String key = "%" + name().toLowerCase() + "%";
            this.formatKey = FormatKey.of(type, key, converter);
        }

        @Override
        public FormatKey formatKeyImpl() {
            return formatKey;
        }

        // Overrides toString() so you can use
        //              "This is a message" + TEXT + "."
        // instead of
        //              "This is a message" + TEXT.getKey() "."
        @Override
        public String toString() {
            return formatKey.getKey();
        }
    }

    // Creating formate keys with enums (using raw FormatKey)
    private enum ExampleEnumFormatKey implements FormatKey {
        TEXT,
        COLOR {
            @Override
            public String replace(String message, Object o) {
                if (o instanceof ChatColor color) return replace(message, color.name());
                return super.replace(message, o);
            }
        };

        private final String key;

        ExampleEnumFormatKey() {
            this.key = "%" + name().toLowerCase() + "%";
        }

        @Override
        public String getKey() {
            return this.key;
        }

        // Overrides toString() so you can use
        //              "This is a message" + TEXT + "."
        // instead of
        //              "This is a message" + TEXT.getKey() "."
        @Override
        public String toString() {
            return this.key;
        }
    }

}
