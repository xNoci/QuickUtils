package me.noci.quickutilities.utils.formatter;

public interface DefaultFormatKey extends FormatKey {

    @Override
    default String replace(String message, Object o) {
        return formatKeyImpl().replace(message, o);
    }

    @Override
    default String getKey() {
        return formatKeyImpl().getKey();
    }

    FormatKey formatKeyImpl();
}
