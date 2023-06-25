package me.noci.quickutilities.utils.formatter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.random.RandomGenerator;

public final class ExampleFormatKey {

    // Creating static format keys
    private static final FormatKey STATIC_FORMAT_KEY = new FormatKey.DefaultKey("%player_health%") {
        @Override
        public String replace(String message, Object o) {
            if (o instanceof Player player) return replace(message, Double.toString(player.getHealth()));
            return super.replace(message, o);
        }
    };

    private ExampleFormatKey() {
    }

    private static void exampleUse() {
        // You can use the keys in messages either by using KEY.getKey()
        // or if the class/enum implementation of FormatKey overrides toString()
        // to return the key instead. You can just use the enum/variable.
        String exampleMessage = "The favourite color of " + FormatKey.PLAYER_NAME +
                "is " + ExampleEnumFormatKey.COLOR.getKey() +
                " his favourite letter is " + ExampleEnumFormatKey.TEXT + " and his favourite word is " + FormatKey.MESSAGE.getKey() + "." +
                " He  currently has " + STATIC_FORMAT_KEY + " lives.";

        for (Player player : Bukkit.getOnlinePlayers()) {
            SomeColor favColor = SomeColor.randomColor();

            MessageFormatter formatter = MessageFormatter.format(exampleMessage);    // Creating Message Formatter
            formatter.apply(ExampleEnumFormatKey.COLOR, favColor);                   // Key via Enum
            formatter.apply(ExampleEnumFormatKey.TEXT, "B");                         // Key via Enum
            formatter.apply(FormatKey.PLAYER_NAME, player);                          // Default key
            formatter.apply(FormatKey.MESSAGE, "Tree");                              // Default key
            formatter.apply(STATIC_FORMAT_KEY, player);                              // Static created format key

            Bukkit.broadcastMessage(formatter.toString());
        }
    }

    // Creating formate keys with enums
    private enum ExampleEnumFormatKey implements FormatKey {
        TEXT,
        COLOR {
            @Override
            public String replace(String message, Object o) {
                if (o instanceof SomeColor color) return replace(message, color.name);
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

    private record SomeColor(String name, int r, int g, int b) {
        public static SomeColor RED = new SomeColor("Red", 255, 0, 0);
        public static SomeColor GREEN = new SomeColor("Green", 0, 255, 0);
        public static SomeColor BLUE = new SomeColor("Blue", 0, 0, 255);

        public static SomeColor[] COLORS = new SomeColor[]{RED, GREEN, BLUE};

        public static SomeColor randomColor() {
            int color = RandomGenerator.getDefault().nextInt(0, 3);
            return COLORS[color];
        }
    }

}
