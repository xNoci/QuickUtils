package me.noci.quickutilities.utils.formatter;

public class MessageFormatter {

    public static MessageFormatter format(String message) {
        return new MessageFormatter(message);
    }

    private String message;

    private MessageFormatter(String message) {
        this.message = message;
    }

    public void apply(FormatKey key, Object object) {
        this.message = key.replace(message, object);
    }

    public void apply(FormatKey key, String value) {
        this.message = key.replace(message, value);
    }

    @Override
    public String toString() {
        return message;
    }

}
