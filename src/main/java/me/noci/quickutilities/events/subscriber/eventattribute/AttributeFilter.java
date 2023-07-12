package me.noci.quickutilities.events.subscriber.eventattribute;

import org.bukkit.event.Event;

@FunctionalInterface
public interface AttributeFilter<T extends Event, A> {
    boolean test(T event, EventAttribute<T, A> attribute);
}
