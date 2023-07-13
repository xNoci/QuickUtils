package me.noci.quickutilities.events.subscriber;

import lombok.Getter;
import lombok.SneakyThrows;
import me.noci.quickutilities.events.subscriber.eventattribute.AttributeRegistryImpl;
import me.noci.quickutilities.events.subscriber.eventattribute.EventAttributeImpl;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Predicate;

public class SubscribedEventImpl<T extends Event> implements SubscribedEvent<T>, EventExecutor, Listener {

    private final Class<T> eventClass;
    private final EventPriority priority;
    private final JavaPlugin plugin;

    private final AttributeRegistryImpl<T> attributeRegistry;
    private final List<Predicate<T>> filters;
    private final EventHandler<T> eventHandler;
    private final List<Expiry<T>> expiries;

    private final Method getHandlerList;
    private boolean active = false;
    @Getter private int calls = 0;
    private boolean expired = false;

    @SneakyThrows
    public SubscribedEventImpl(Class<T> eventType, EventHandler<T> eventHandler, EventPriority priority, JavaPlugin plugin, AttributeRegistryImpl<T> attributeRegistry, List<Predicate<T>> filters, List<Expiry<T>> expiries) {
        this.eventClass = eventType;
        this.priority = priority;
        this.plugin = plugin;
        this.eventHandler = eventHandler;
        this.filters = filters;
        this.expiries = expiries;
        this.attributeRegistry = attributeRegistry;

        this.getHandlerList = eventClass.getMethod("getHandlerList");
        register();
    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        if (expired) return;
        if (eventClass != event.getClass()) return;
        T typedEvent = eventClass.cast(event);

        boolean expirePre = expiries.stream()
                .filter(expiry -> expiry.stage() == Expiry.ExpiryStage.PRE)
                .anyMatch(expiry -> expiry.expire(this, typedEvent));

        if (expirePre) {
            expired = true;
            unsubscribe();
            return;
        }

        if (!active) {
            unsubscribe();
            return;
        }

        for (Predicate<T> filter : filters) {
            if (filter.test(typedEvent)) return;
        }

        for (EventAttributeImpl<T, ?> attribute : attributeRegistry.getAttributes()) {
            attribute.updateValue(typedEvent);
            if (attribute.test(typedEvent)) return;
        }

        eventHandler.handle(typedEvent, attributeRegistry);
        calls++;

        boolean expirePost = expiries.stream()
                .filter(expiry -> expiry.stage() == Expiry.ExpiryStage.POST)
                .anyMatch(expiry -> expiry.expire(this, typedEvent));

        if (expirePost) {
            expired = true;
            unsubscribe();
            return;
        }
    }

    @Override
    public Class<T> getEventType() {
        return this.eventClass;
    }

    @Override
    @SneakyThrows
    public void unsubscribe() {
        active = false;
        HandlerList handlerList = (HandlerList) getHandlerList.invoke(null);
        handlerList.unregister(this);
    }

    @Override
    public void register() {
        if (active) return;
        active = true;
        Bukkit.getPluginManager().registerEvent(eventClass, this, priority, this, plugin);
    }

    @Override
    public boolean isActive() {
        return active && !expired;
    }

    @Override
    public boolean isExpired() {
        return expired;
    }
}
