package me.noci.quickutilities.quicktab;

import me.noci.quickutilities.events.Events;
import me.noci.quickutilities.events.subscriber.SubscribedEvent;
import me.noci.quickutilities.quicktab.builder.QuickTabBuilder;
import me.noci.quickutilities.utils.Require;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UpdatingTabList {

    private final QuickTabBuilder builder;
    private final SubscribedEvent<PlayerJoinEvent> joinEvent;
    private final SubscribedEvent<PlayerQuitEvent> quitEvent;

    protected UpdatingTabList(QuickTabBuilder builder) {
        Require.checkState(builder != null, "TabListTeamBuilder cannot be null.");
        this.builder = builder;
        this.joinEvent = Events.subscribe(PlayerJoinEvent.class, EventPriority.MONITOR).handle(event -> update());
        this.quitEvent = Events.subscribe(PlayerQuitEvent.class, EventPriority.MONITOR).handle(event -> update());
    }

    public void delete() {
        joinEvent.unsubscribe();
        quitEvent.unsubscribe();
    }

    public void update(Player player) {
        QuickTab.internalUpdate(player, builder);
    }

    public void update() {
        Bukkit.getOnlinePlayers().forEach(this::update);
    }

}
