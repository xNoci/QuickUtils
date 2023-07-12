package me.noci.quickutilities.events.subscriber;

import org.bukkit.event.Event;

public interface SubscribedEvent<T extends Event> {

    Class<T> getEventType();

    void unsubscribe();

    void register();

    boolean isActive();

    boolean isExpired();

}
