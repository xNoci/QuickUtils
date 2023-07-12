package me.noci.quickutilities.events.subscriber;

import me.noci.quickutilities.events.subscriber.eventattribute.AttributeRegistry;
import org.bukkit.event.Event;

@FunctionalInterface
public interface EventHandler<T extends Event> {
    void handle(T event, AttributeRegistry<T> attributeRegistry);
}
