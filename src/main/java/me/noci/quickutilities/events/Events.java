package me.noci.quickutilities.events;

import me.noci.quickutilities.events.subscriber.builder.EventBuilder;
import me.noci.quickutilities.events.subscriber.builder.SingleEventBuilder;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;

public class Events {

    public static <E extends Event> EventBuilder<E> subscribe(Class<E> event) {
        return subscribe(event, EventPriority.NORMAL);
    }

    public static <E extends Event> EventBuilder<E> subscribe(Class<E> event, EventPriority priority) {
        return new SingleEventBuilder<>(event, priority);
    }

}
