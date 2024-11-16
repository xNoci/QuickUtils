package me.noci.quickutilities.scoreboard;

import me.noci.quickutilities.events.Events;
import me.noci.quickutilities.events.subscriber.SubscribedEvent;
import me.noci.quickutilities.utils.Require;
import me.noci.quickutilities.utils.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

public class UpdatingScoreboard<T> {

    private final QuickBoard<T> handle;
    private final ScoreboardUpdate<T> scoreboardHandler;
    private final BukkitTask runnable;
    private final SubscribedEvent<PlayerJoinEvent> joinEvent;
    private final SubscribedEvent<PlayerQuitEvent> quitEvent;

    protected UpdatingScoreboard(QuickBoard<T> quickBoard, long ticks, ScoreboardUpdate<T> scoreboardHandler) {
        Require.checkState(scoreboardHandler != null, "TabListTeamBuilder cannot be null.");
        this.handle = quickBoard;
        this.scoreboardHandler = scoreboardHandler;
        this.joinEvent = Events.subscribe(PlayerJoinEvent.class, EventPriority.MONITOR).handle(event -> updateAll());
        this.quitEvent = Events.subscribe(PlayerQuitEvent.class, EventPriority.MONITOR).handle(event -> {
            Bukkit.getOnlinePlayers().stream()
                    .filter(player -> !player.getUniqueId().equals(event.getPlayer().getUniqueId()))
                    .forEach(this::update);
        });
        this.runnable = ticks <= 0 ? null : Scheduler.repeat(ticks, this::updateAll);
    }

    public void delete() {
        joinEvent.unsubscribe();
        quitEvent.unsubscribe();
        if (runnable != null) runnable.cancel();
    }

    public void update(Player player) {
        Scoreboard<T> scoreboard = handle.getScoreboardInternal(player);
        scoreboardHandler.update(player, scoreboard);
    }

    public void updateAll() {
        Bukkit.getOnlinePlayers().forEach(this::update);
    }

}
