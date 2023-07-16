package me.noci.quickutilities.scoreboard;

import com.google.common.collect.Maps;
import me.noci.quickutilities.events.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class QuickBoard {

    private static final HashMap<UUID, StringScoreboard> PLAYER_BOARDS = Maps.newHashMap();

    static {
        Events.subscribe(PlayerQuitEvent.class).handle(event -> PLAYER_BOARDS.remove(event.getPlayer().getUniqueId()));
    }

    public static Scoreboard<String> getScoreboard(Player player) {
        Objects.requireNonNull(player, "Player cannot be null");
        StringScoreboard scoreboard = PLAYER_BOARDS.get(player.getUniqueId());
        if (scoreboard == null || scoreboard.isDeleted()) {
            scoreboard = new StringScoreboard(player);
            PLAYER_BOARDS.put(player.getUniqueId(), scoreboard);
        }
        return scoreboard;
    }

    private QuickBoard() {
    }

}
