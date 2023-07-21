package me.noci.quickutilities.scoreboard;

import com.google.common.collect.Maps;
import me.noci.quickutilities.events.Events;
import me.noci.quickutilities.utils.BukkitUnit;
import me.noci.quickutilities.utils.Require;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.UUID;

public final class QuickBoard {

    private static final HashMap<UUID, StringScoreboard> PLAYER_BOARDS = Maps.newHashMap();
    private static UpdatingScoreboard updatingScoreboard = null;

    static {
        Events.subscribe(PlayerJoinEvent.class, EventPriority.LOWEST).handle(event -> getScoreboardInternal(event.getPlayer()).delete());
    }

    /**
     * Get a scoreboard instance for a specific player
     *
     * @param player the player which scoreboard instance should be retrieved
     * @return a scoreboard instance
     * @throws IllegalStateException when an updating scoreboard is set
     * @throws NullPointerException  when {@param player} is null
     */
    public static Scoreboard<String> getScoreboard(Player player) {
        checkScoreboardNotSet();
        return getScoreboardInternal(player);
    }

    /**
     * Update the scoreboard of a player using the current updating scoreboard instance
     *
     * @param player his scoreboard will be updated
     * @throws IllegalStateException when no updating scoreboard is set
     */
    public static void update(Player player) {
        checkScoreboardSet();
        updatingScoreboard.update(player);
    }

    /**
     * Update the scoreboard of all online players using the current updating scoreboard instance
     *
     * @throws IllegalStateException when no updating scoreboard is set
     */
    public static void updateAll() {
        checkScoreboardSet();
        updatingScoreboard.update();
    }

    /**
     * Setting a new updating scoreboard instance.
     * The scoreboard for each player will be updated every time a player leaves or joins.
     * It also updates in a specific interval which can be set using {@param value} and {@param timeUnit}.
     *
     * @param plugin     which sets the updating scoreboard instance
     * @param value      interval to update scoreboard
     * @param timeUnit   interval unit
     * @param scoreboard the scoreboard which will be set for every player
     * @throws IllegalStateException when an updating scoreboard instance is already set
     */
    public static synchronized void setUpdatingScoreboard(JavaPlugin plugin, int value, BukkitUnit timeUnit, ScoreboardUpdate<String> scoreboard) {
        checkScoreboardNotSet();
        updatingScoreboard = new UpdatingScoreboard(plugin, timeUnit.toTicks(value), scoreboard);
    }

    /**
     * Remove the current updating scoreboard instance. This also allows to set a new instance using {@link QuickBoard#setUpdatingScoreboard(JavaPlugin, int, BukkitUnit, ScoreboardUpdate)}
     */
    public static synchronized void removeUpdatingScoreboard() {
        checkScoreboardSet();
        updatingScoreboard.delete();
        updatingScoreboard = null;
    }

    /**
     * This method is used internally to get a scoreboard instance of a {@link Player}.
     * <br> It does not check whether a {@link UpdatingScoreboard} is currently set or not.
     *
     * @param player the player which scoreboard instance should be retrieved
     * @return a scoreboard instance
     * @throws NullPointerException when {@param player} is null
     */
    @ApiStatus.Internal
    static Scoreboard<String> getScoreboardInternal(Player player) {
        Require.nonNull(player, "Player cannot be null");
        StringScoreboard scoreboard = PLAYER_BOARDS.get(player.getUniqueId());
        if (scoreboard == null || scoreboard.isDeleted()) {
            scoreboard = new StringScoreboard(player);
            PLAYER_BOARDS.put(player.getUniqueId(), scoreboard);
        }
        return scoreboard;
    }

    private static void checkScoreboardNotSet() {
        Require.checkState(updatingScoreboard == null, "This is only possible if updating scoreboard is not set");
    }

    private static void checkScoreboardSet() {
        Require.checkState(updatingScoreboard != null, "This is only possible if updating scoreboard is set");
    }

    private QuickBoard() {
    }

}
