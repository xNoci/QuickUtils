package me.noci.quickutilities.utils.logfilter;

import me.noci.quickutilities.utils.Require;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

public class LogFilters {

    public static void addFilter(LogFilter filter) {
        Require.nonNull(filter, "Filter cannot be null");
        Logger logger = (Logger) LogManager.getRootLogger();
        logger.addFilter(new Filter(filter));
    }

    private static class Filter extends AbstractFilter {

        private final LogFilter filter;

        private Filter(LogFilter filter) {
            this.filter = filter;
        }

        @Override
        public Result filter(Logger logger, Level level, Marker marker, String s, Object... objects) {
            return Result.NEUTRAL;
        }

        @Override
        public Result filter(Logger logger, Level level, Marker marker, Object o, Throwable throwable) {
            return Result.NEUTRAL;
        }

        @Override
        public Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
            return Result.NEUTRAL;
        }

        @Override
        public Result filter(LogEvent event) {
            return event == null ? Result.NEUTRAL : match(event.getMessage().getFormattedMessage());
        }

        private Result match(String message) {
            return switch (filter.filter(message)) {
                case ACCEPT -> Result.ACCEPT;
                case DENY -> Result.DENY;
                case NEUTRAL -> Result.NEUTRAL;
            };
        }

    }

}
