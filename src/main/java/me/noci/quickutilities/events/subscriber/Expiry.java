package me.noci.quickutilities.events.subscriber;

import org.bukkit.event.Event;

@FunctionalInterface
public interface Expiry<T extends Event> {

    static <T extends Event> Expiry<T> pre(Expiry<T> expiry) {
        return expiry;
    }

    static <T extends Event> Expiry<T> post(Expiry<T> expiry) {
        return new Expiry<>() {
            @Override
            public boolean expire(SubscribedEventImpl<T> handler, T event) {
                return expiry.expire(handler, event);
            }

            @Override
            public ExpiryStage stage() {
                return ExpiryStage.POST;
            }
        };
    }

    boolean expire(SubscribedEventImpl<T> handler, T event);

    default ExpiryStage stage() {
        return ExpiryStage.PRE;
    }

    enum ExpiryStage {
        PRE,
        POST
    }


}
