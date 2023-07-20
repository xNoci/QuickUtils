package me.noci.quickutilities.utils.logfilter;

public interface LogFilter {

    Result filter(String message);

    enum Result {
        ACCEPT,
        NEUTRAL,
        DENY;
    }

}
