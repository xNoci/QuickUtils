package me.noci.quickutilities.quicktab;

import me.noci.quickutilities.events.Events;
import me.noci.quickutilities.events.subscriber.SubscribedEvent;
import me.noci.quickutilities.quicktab.builder.QuickTabBuilder;
import me.noci.quickutilities.utils.Require;
import me.noci.quickutilities.utils.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

public class UpdatingTabList {

    private final QuickTabBuilder builder;
    private final BukkitTask runnable;
    private final SubscribedEvent<PlayerJoinEvent> joinEvent;
    private final SubscribedEvent<PlayerQuitEvent> quitEvent;

    protected UpdatingTabList(QuickTabBuilder builder, long ticks) {
        Require.checkState(builder != null, "TabListTeamBuilder cannot be null.");
        this.builder = builder;
        this.joinEvent = Events.subscribe(PlayerJoinEvent.class, EventPriority.MONITOR).handle(event -> update());
        this.quitEvent = Events.subscribe(PlayerQuitEvent.class, EventPriority.MONITOR).handle(event -> update());
        this.runnable = ticks <= 0 ? null : Scheduler.repeat(ticks, this::update);
    }

    public void delete() {
        joinEvent.unsubscribe();
        quitEvent.unsubscribe();
        if (runnable != null) runnable.cancel();
    }

    public void update(Player player) {
        QuickTab.internalUpdate(player, builder);
    }

    public void update() {
        Bukkit.getOnlinePlayers().forEach(this::update);
    }

}
