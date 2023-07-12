package me.noci.quickutilities.events.subscriber.eventattribute;

import com.google.common.collect.Maps;
import org.bukkit.event.Event;

import java.util.Collection;
import java.util.HashMap;

public class AttributeRegistryImpl<E extends Event> implements AttributeRegistry<E> {

    private final HashMap<String, EventAttributeImpl<E, ?>> attributes = Maps.newHashMap();

    public void add(String name, EventAttributeImpl<E, ?> attribute) {
        if (attributes.containsKey(key(name)))
            throw new IllegalStateException("Cannot add a attribute with the name '%s' twice.".formatted(name));
        attributes.put(key(name), attribute);
    }

    @Override
    public <A> EventAttributeImpl<E, A> get(String name, Class<A> attributeType) {
        EventAttributeImpl<E, ?> attribute = get(name);
        if (!attribute.getType().equals(attributeType))
            throw new IllegalStateException("Could not get attribute with name '%s' and type '%s'.".formatted(name, attributeType.getName()));
        return (EventAttributeImpl<E, A>) attribute;
    }

    @Override
    public <A> EventAttributeImpl<E, A> get(Class<A> attributeType) {
        for (EventAttributeImpl<E, ?> value : attributes.values()) {
            if (value.getType().equals(attributeType)) return (EventAttributeImpl<E, A>) value;
        }
        throw new IllegalStateException("Could not find attribute with type '%s'.".formatted(attributeType.getName()));
    }

    @Override
    public EventAttributeImpl<E, ?> get(String name) {
        return check(name);
    }

    public Collection<EventAttributeImpl<E, ?>> getAttributes() {
        return attributes.values();
    }

    private EventAttributeImpl<E, ?> check(String name) {
        if (!attributes.containsKey(key(name)))
            throw new IllegalStateException("Cannot get a attribute with name '%s'.".formatted(name));
        return attributes.get(key(name));
    }

    private String key(String name) {
        return name.toLowerCase();
    }

}
