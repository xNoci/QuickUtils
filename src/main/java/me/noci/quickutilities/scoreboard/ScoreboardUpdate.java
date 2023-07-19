package me.noci.quickutilities.scoreboard;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface ScoreboardUpdate<T> {
    void update(Player player, Scoreboard<T> scoreboard);
}
