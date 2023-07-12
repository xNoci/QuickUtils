package me.noci.quickutilities.events.subscriber.eventattribute;

import org.bukkit.event.Event;

public interface EventAttribute<E extends Event, A> {

    boolean isNull();

    boolean notNull();

    A get();

    A or(A def);

    Class<A> getType();

}
