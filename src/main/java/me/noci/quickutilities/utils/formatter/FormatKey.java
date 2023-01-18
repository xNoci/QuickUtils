package me.noci.quickutilities.utils.formatter;

public interface FormatKey {

    String getKey();

    default String replace(String message, Object o) {
        return replace(message, o.toString());
    }

    default String replace(String message, String value) {
        return message.replace(getKey(), value);
    }

}
