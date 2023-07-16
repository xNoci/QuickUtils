package me.noci.quickutilities.events.subscriber.builder;

import com.google.common.collect.Lists;
import me.noci.quickutilities.QuickUtils;
import me.noci.quickutilities.events.subscriber.EventHandler;
import me.noci.quickutilities.events.subscriber.Expiry;
import me.noci.quickutilities.events.subscriber.SubscribedEvent;
import me.noci.quickutilities.events.subscriber.SubscribedEventImpl;
import me.noci.quickutilities.events.subscriber.eventattribute.AttributeFilter;
import me.noci.quickutilities.events.subscriber.eventattribute.AttributeRegistryImpl;
import me.noci.quickutilities.events.subscriber.eventattribute.EventAttributeImpl;
import me.noci.quickutilities.utils.BukkitUnit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class DefaultEventBuilder<T extends Event> implements EventBuilder<T> {

    private final AttributeRegistryImpl<T> attributeRegistry = new AttributeRegistryImpl<>();
    private final List<Predicate<T>> filters = Lists.newArrayList();
    private final List<Expiry<T>> expiries = Lists.newArrayList();
    private final Class<T> eventType;
    private final EventPriority priority;
    private long delayTicks = 0;
    private JavaPlugin plugin;

    public DefaultEventBuilder(Class<T> event, EventPriority priority) {
        this.eventType = event;
        this.priority = priority;
        this.plugin = QuickUtils.instance();
    }

    @Override
    public EventBuilder<T> expireAfter(int value, BukkitUnit timeUnit) {
        long expiredTime = Math.addExact(System.currentTimeMillis(), timeUnit.toMilliseconds(value));
        expiries.add(Expiry.pre((handler, e) -> System.currentTimeMillis() >= expiredTime));
        return this;
    }

    @Override
    public EventBuilder<T> expireAfter(int uses) {
        expiries.add(Expiry.pre((handler, event) -> handler.getCalls() >= uses));
        return this;
    }

    @Override
    public EventBuilder<T> expireWhen(Predicate<T> expire) {
        expiries.add(Expiry.post((handler, e) -> expire.test(e)));
        return this;
    }


    @Override
    public EventBuilder<T> filter(Predicate<T> filter) {
        filters.add(filter);
        return this;
    }

    @Override
    public <A> EventBuilder<T> attribute(String name, Class<A> attributeClass, Function<T, A> attribute) {
        attributeRegistry.add(name, new EventAttributeImpl<>(attributeClass, attribute));
        return this;
    }

    @Override
    public <A> EventBuilder<T> filterAttribute(String name, Class<A> attributeClass, AttributeFilter<T, A> filter) {
        attributeRegistry.get(name, attributeClass).addFilter(filter);
        return this;
    }

    @Override
    public EventBuilder<T> delay(int value, BukkitUnit timeUnit) {
        this.delayTicks = timeUnit.toTicks(value);
        return this;
    }

    @Override
    public EventBuilder<T> plugin(JavaPlugin plugin) {
        this.plugin = plugin;
        return this;
    }

    @Override
    public SubscribedEvent<T> handle(EventHandler<T> eventHandler) {
        return new SubscribedEventImpl<>(eventType, eventHandler, delayTicks, priority, plugin, attributeRegistry, filters, expiries);
    }

}
