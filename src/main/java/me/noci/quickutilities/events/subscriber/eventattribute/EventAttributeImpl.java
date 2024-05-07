package me.noci.quickutilities.events.subscriber.eventattribute;

import com.google.common.collect.Lists;
import org.bukkit.event.Event;

import java.util.List;
import java.util.function.Function;

public class EventAttributeImpl<E extends Event, A> implements EventAttribute<E, A> {

    private final Class<A> attributeType;
    private final Function<E, A> attributeFunction;
    private final List<AttributeFilter<E, A>> filters = Lists.newArrayList();
    private A value = null;

    public EventAttributeImpl(Class<A> attributeType, Function<E, A> attribute) {
        this.attributeType = attributeType;
        this.attributeFunction = attribute;
    }

    public void updateValue(E event) {
        this.value = attributeFunction.apply(event);
    }

    public void addFilter(AttributeFilter<E, A> filter) {
        this.filters.add(filter);
    }

    public boolean test(E event) {
        for (AttributeFilter<E, A> filter : filters) {
            if (!filter.test(event, this)) return false;
        }
        return true;
    }

    @Override
    public boolean isNull() {
        return this.value == null;
    }

    @Override
    public boolean notNull() {
        return this.value != null;
    }

    @Override
    public A get() {
        return this.value;
    }

    @Override
    public A or(A def) {
        return notNull() ? this.value : def;
    }

    @Override
    public Class<A> getType() {
        return this.attributeType;
    }

}
