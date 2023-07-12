package me.noci.quickutilities.events;

import me.noci.quickutilities.events.subscriber.builder.DefaultEventBuilder;
import me.noci.quickutilities.events.subscriber.builder.EventBuilder;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

public class Events {

    public static <T extends Event> EventBuilder<T> subscribe(Class<T> event) {
        return subscribe(event, EventPriority.NORMAL);
    }

    public static <T extends Event> EventBuilder<T> subscribe(Class<T> event, EventPriority priority) {
        return new DefaultEventBuilder<>(event, priority);
    }

}
