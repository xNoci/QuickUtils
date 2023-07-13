package me.noci.quickutilities.events.subscriber.builder;

import me.noci.quickutilities.events.subscriber.EventHandler;
import me.noci.quickutilities.events.subscriber.SubscribedEvent;
import me.noci.quickutilities.events.subscriber.eventattribute.AttributeFilter;
import me.noci.quickutilities.utils.BukkitUnit;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface EventBuilder<T extends Event> {

    /**
     * Set the plugin which will be used to register the event.
     * Default: QuickUtils
     * @param plugin
     * @return
     */
    EventBuilder<T> plugin(JavaPlugin plugin);

    /**
     * Set the amount of time after which the event will be expired.
     * If the event is expired it cannot be activated again.
     * @param value
     * @param timeUnit
     * @return
     */
    EventBuilder<T> expireAfter(int value, BukkitUnit timeUnit);

    /**
     * Set the amount of times this event can be called.
     * As soon as the addon is expired it cannot be activated again.
     * @param uses
     * @return
     */
    EventBuilder<T> expireAfter(int uses);

    /**
     * Set a condition after which the event will be expired.
     * This condition check will happen after the execution of the event.
     * If the event is expired it cannot be activated again.
     * @param expire
     * @return
     */
    EventBuilder<T> expireWhen(Predicate<T> expire);

    /**
     * Set a condition that has to be true before the execution of the event will happen.
     * @param filter
     * @return
     */
    EventBuilder<T> filter(Predicate<T> filter);

    /**
     * Set an attribute that can used either in a filter or in a handler
     * @param name
     * @param attributeClass
     * @param attribute
     * @return
     * @param <A>
     */
    <A> EventBuilder<T> attribute(String name, Class<A> attributeClass, Function<T, A> attribute);

    /**
     * Set an attribute that can used either in a filter or in a handler
     * @param attributeClass
     * @param attribute
     * @return
     * @param <A>
     */
    default <A> EventBuilder<T> attribute(Class<A> attributeClass, Function<T, A> attribute) {
        return attribute(attributeClass.getSimpleName(), attributeClass, attribute);
    }

    /**
     * A filter with access to the given attribute
     * @param name
     * @param attributeClass
     * @param filter
     * @return
     * @param <A>
     */
    <A> EventBuilder<T> filterAttribute(String name, Class<A> attributeClass, AttributeFilter<T, A> filter);

    /**
     * A filter with access to the given attribute
     * @param attributeClass
     * @param filter
     * @return
     * @param <A>
     */
    default <A> EventBuilder<T> filterAttribute(Class<A> attributeClass, AttributeFilter<T, A> filter) {
        return filterAttribute(attributeClass.getSimpleName(), attributeClass, filter);
    }

    /**
     * Handle the event with access to all attributes
     * @param event
     * @return
     */
    SubscribedEvent<T> handle(EventHandler<T> event);

    /**
     * Handle the event without access to attributes
      * @param event
     * @return
     */
    default SubscribedEvent<T> handle(Consumer<T> event) {
        return handle((e, eac) -> event.accept(e));
    }

}
