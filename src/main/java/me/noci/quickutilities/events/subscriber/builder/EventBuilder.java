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

    EventBuilder<T> plugin(JavaPlugin plugin);

    EventBuilder<T> expireAfter(int value, BukkitUnit timeUnit);

    EventBuilder<T> expireAfter(int uses);

    EventBuilder<T> expireWhen(Predicate<T> expire);

    EventBuilder<T> filter(Predicate<T> filter);

    <A> EventBuilder<T> attribute(String name, Class<A> attributeClass, Function<T, A> attribute);

    default <A> EventBuilder<T> attribute(Class<A> attributeClass, Function<T, A> attribute) {
        return attribute(attributeClass.getSimpleName(), attributeClass, attribute);
    }

    <A> EventBuilder<T> filterAttribute(String name, Class<A> attributeClass, AttributeFilter<T, A> filter);

    default <A> EventBuilder<T> filterAttribute(Class<A> attributeClass, AttributeFilter<T, A> filter) {
        return filterAttribute(attributeClass.getSimpleName(), attributeClass, filter);
    }

    SubscribedEvent<T> handle(EventHandler<T> event);

    default SubscribedEvent<T> handle(Consumer<T> event) {
        return handle((e, eac) -> event.accept(e));
    }

}
