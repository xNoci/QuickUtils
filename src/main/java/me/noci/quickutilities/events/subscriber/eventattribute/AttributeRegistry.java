package me.noci.quickutilities.events.subscriber.eventattribute;

import org.bukkit.event.Event;

public interface AttributeRegistry<E extends Event> {

    <A> EventAttribute<E, A> get(String name, Class<A> attributeType);

    <A> EventAttribute<E, A> get(Class<A> attributeType);

    EventAttribute<E, ?> get(String name);

}
