package me.noci.quickutilities.events.core;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;

public abstract class CoreCancellableEvent extends CoreEvent implements Cancellable {

    @Getter @Setter private boolean cancelled;

    public CoreCancellableEvent() {
        this(false);
    }

    public CoreCancellableEvent(boolean cancelled) {
        this(false, false);
    }

    public CoreCancellableEvent(boolean cancelled, boolean async) {
        super(async);
        this.cancelled = cancelled;
    }

}
