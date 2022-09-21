package me.noci.quickutilities.events.core;

import lombok.Getter;
import org.bukkit.entity.Player;

public abstract class CorePlayerEvent extends CoreEvent {

    @Getter private final Player player;

    public CorePlayerEvent(Player player) {
        this(player, false);
    }

    public CorePlayerEvent(Player player, boolean async) {
        super(async);
        this.player = player;
    }

}
