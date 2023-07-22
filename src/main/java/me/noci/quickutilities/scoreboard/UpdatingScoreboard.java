package me.noci.quickutilities.scoreboard;

import me.noci.quickutilities.events.Events;
import me.noci.quickutilities.events.subscriber.SubscribedEvent;
import me.noci.quickutilities.utils.Require;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class UpdatingScoreboard {

    private final ScoreboardUpdate<String> scoreboardHandler;
    private final BukkitTask runnable;
    private final SubscribedEvent<PlayerJoinEvent> joinEvent;
    private final SubscribedEvent<PlayerQuitEvent> quitEvent;

    protected UpdatingScoreboard(JavaPlugin plugin, long ticks, ScoreboardUpdate<String> scoreboardHandler) {
        Require.checkState(scoreboardHandler != null, "TabListTeamBuilder cannot be null.");
        this.scoreboardHandler = scoreboardHandler;
        this.joinEvent = Events.subscribe(PlayerJoinEvent.class, EventPriority.MONITOR).handle(event -> update());
        this.quitEvent = Events.subscribe(PlayerQuitEvent.class, EventPriority.MONITOR).handle(event -> {
            Bukkit.getOnlinePlayers().stream()
                    .filter(player -> !player.getUniqueId().equals(event.getPlayer().getUniqueId()))
                    .forEach(this::update);
        });
        this.runnable = Bukkit.getScheduler().runTaskTimer(plugin, this::update, 0, ticks);
    }

    public void delete() {
        joinEvent.unsubscribe();
        quitEvent.unsubscribe();
        runnable.cancel();
    }

    public void update(Player player) {
        Scoreboard<String> scoreboard = QuickBoard.getScoreboardInternal(player);
        scoreboardHandler.update(player, scoreboard);
    }

    public void update() {
        Bukkit.getOnlinePlayers().forEach(this::update);
    }

}
