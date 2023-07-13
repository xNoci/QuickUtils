package me.noci.quickutilities.events.subscriber;

import org.bukkit.event.Event;

public interface SubscribedEvent<T extends Event> {

    /**
     * Type tp which event will be listened
     * @return
     */
    Class<T> getEventType();

    /**
     * Unsubscribe the event
     */
    void unsubscribe();

    /**
     * Register the event if not registered.
     * Cannot register if event is expired
     */
    void register();

    /**
     * Check if the event is active and not expired
     * @return
     */
    boolean isActive();

    /**
     * Check if the event is already expired
     * @return
     */
    boolean isExpired();

}
