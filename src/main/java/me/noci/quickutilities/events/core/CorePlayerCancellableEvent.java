package me.noci.quickutilities.events.core;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public abstract class CorePlayerCancellableEvent extends CorePlayerEvent implements Cancellable {

    @Getter @Setter private boolean cancelled;

    public CorePlayerCancellableEvent(Player player) {
        this(player, false, false);
    }

    public CorePlayerCancellableEvent(Player player, boolean cancelled) {
        this(player, cancelled, false);
    }

    public CorePlayerCancellableEvent(Player player, boolean cancelled, boolean async) {
        super(player, async);
        this.cancelled = cancelled;
    }

}
