package me.noci.quickutilities.scoreboard;

import com.google.common.collect.Maps;
import me.noci.quickutilities.events.Events;
import me.noci.quickutilities.quicktab.builder.QuickTabBuilder;
import me.noci.quickutilities.utils.time.BukkitUnit;
import me.noci.quickutilities.utils.Require;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

public final class QuickBoard<T> {

    private static volatile QuickBoard<?> instance = null;

    private final Class<T> type;
    private final HashMap<UUID, Scoreboard<T>> PLAYER_BOARDS = Maps.newHashMap();
    private final Function<Player, Scoreboard<T>> scoreboardSupplier;
    private UpdatingScoreboard<T> updatingScoreboard = null;

    private QuickBoard(Class<T> type, Function<Player, Scoreboard<T>> scoreboardSupplier) {
        this.type = type;
        this.scoreboardSupplier = scoreboardSupplier;
        Events.subscribe(PlayerQuitEvent.class, EventPriority.MONITOR).handle(event -> {
            var scoreboard = PLAYER_BOARDS.remove(event.getPlayer().getUniqueId());
            if (scoreboard != null) {
                scoreboard.delete();
            }
        });
    }

    /**
     * Create a new instance {@link QuickBoard} of type {@link String}, or returns it if already created.
     *
     * @return {@link QuickBoard<String>}
     * @throws IllegalStateException if the current {@link QuickBoard} instance is not of type {@link String}.
     */
    @SuppressWarnings("unchecked")
    public static QuickBoard<String> string() {
        if (instance == null) {
            synchronized (QuickBoard.class) {
                if (instance == null) {
                    instance = new QuickBoard<>(String.class, StringScoreboard::new);
                }
            }
        }

        if (instance.type != String.class) {
            throw new IllegalStateException(
                    "Cannot access quickboard instance. QuickBoard is of type '%s' instead of type '%s'."
                            .formatted(instance.type.getSimpleName(), String.class.getSimpleName())
            );
        }

        return (QuickBoard<String>) instance;
    }

    /**
     * Create a new instance {@link QuickBoard} of type {@link Component}, or returns it if already created.
     *
     * @return {@link QuickBoard<Component>}
     * @throws IllegalStateException if the current {@link QuickBoard} instance is not of type {@link Component}.
     */
    @SuppressWarnings("unchecked")
    public static QuickBoard<Component> component() {
        if (instance == null) {
            synchronized (QuickBoard.class) {
                if (instance == null) {
                    instance = new QuickBoard<>(Component.class, ComponentScoreboard::new);
                }
            }
        }

        if (instance.type != Component.class) {
            throw new IllegalStateException(
                    "Cannot access quickboard instance. QuickBoard is of type '%s' instead of type '%s'."
                            .formatted(instance.type.getSimpleName(), Component.class.getSimpleName())
            );
        }

        return (QuickBoard<Component>) instance;
    }

    /**
     * Replace the current {@link UpdatingScoreboard<T>}
     *
     * <br>
     * <br>
     * The scoreboard for each player will be updated every time a player leaves or joins.
     *
     * <br>
     * <br> When {@param interval} <= 0 then, the scoreboard will only update when player joins the server.
     *
     * @param scoreboard The {@link QuickTabBuilder} which should be used
     */
    public void replaceUpdatingScoreboard(ScoreboardUpdate<T> scoreboard) {
        replaceUpdatingScoreboard(-1, BukkitUnit.TICKS, scoreboard);
    }

    /**
     * Replace the current {@link UpdatingScoreboard<T>}
     *
     * <br>
     * <br>
     * The scoreboard for each player will be updated every time a player leaves or joins.
     * It also updates in a specific interval which can be set using {@param value} and {@param timeUnit}.
     *
     * <br>
     * <br> When {@param interval} <= 0 then, the scoreboard will only update when player joins the server.
     *
     * @param scoreboard The {@link QuickTabBuilder} which should be used
     * @param value      interval to update scoreboard
     * @param timeUnit   interval unit
     */
    public void replaceUpdatingScoreboard(int value, BukkitUnit timeUnit, ScoreboardUpdate<T> scoreboard) {
        if (updatingScoreboard != null) {
            removeUpdatingScoreboard();
        }

        setUpdatingScoreboard(value, timeUnit, scoreboard);
    }

    /**
     * Remove the current updating scoreboard instance. This also allows to set a new instance using {@link QuickBoard#setUpdatingScoreboard(int, BukkitUnit, ScoreboardUpdate)}
     */
    public synchronized void removeUpdatingScoreboard() {
        checkScoreboardSet();
        updatingScoreboard.delete();
        updatingScoreboard = null;
    }

    /**
     * Setting a new updating scoreboard instance.
     * <br>
     * <br>
     * The scoreboard for each player will be updated every time a player leaves or joins.
     *
     * @param scoreboard the scoreboard which will be set for every player
     * @throws IllegalStateException when an updating scoreboard instance is already set
     */
    public synchronized void setUpdatingScoreboard(ScoreboardUpdate<T> scoreboard) {
        setUpdatingScoreboard(-1, BukkitUnit.TICKS, scoreboard);
    }

    /**
     * Setting a new updating scoreboard instance.
     * <br>
     * <br>
     * The scoreboard for each player will be updated every time a player leaves or joins.
     * It also updates in a specific interval which can be set using {@param value} and {@param timeUnit}.
     * <br>
     * <br> When {@param interval} <= 0 then, the scoreboard will only update when player joins the server.
     *
     * @param value      interval to update scoreboard
     * @param timeUnit   interval unit
     * @param scoreboard the scoreboard which will be set for every player
     * @throws IllegalStateException when an updating scoreboard instance is already set
     */
    public synchronized void setUpdatingScoreboard(int value, BukkitUnit timeUnit, ScoreboardUpdate<T> scoreboard) {
        checkScoreboardNotSet();
        updatingScoreboard = new UpdatingScoreboard<>(this, value <= 0 ? -1 : timeUnit.toTicks(value), scoreboard);
    }

    /**
     * Get a scoreboard instance for a specific player
     *
     * @param player the player which scoreboard instance should be retrieved
     * @return a scoreboard instance
     * @throws IllegalStateException when an updating scoreboard is set
     * @throws NullPointerException  when {@param player} is null
     */
    public Scoreboard<T> getScoreboard(Player player) {
        checkScoreboardNotSet();
        return getScoreboardInternal(player);
    }

    /**
     * Update the scoreboard of a player using the current updating scoreboard instance
     *
     * @param player his scoreboard will be updated
     * @throws IllegalStateException when no updating scoreboard is set
     */
    public void update(Player player) {
        checkScoreboardSet();
        updatingScoreboard.update(player);
    }

    /**
     * Update the scoreboard of all online players using the current updating scoreboard instance
     *
     * @throws IllegalStateException when no updating scoreboard is set
     */
    public void updateAll() {
        checkScoreboardSet();
        updatingScoreboard.updateAll();
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
    Scoreboard<T> getScoreboardInternal(Player player) {
        Require.nonNull(player, "Player cannot be null");
        Scoreboard<T> scoreboard = PLAYER_BOARDS.get(player.getUniqueId());
        if (scoreboard == null || scoreboard.isDeleted()) {
            scoreboard = scoreboardSupplier.apply(player);
            PLAYER_BOARDS.put(player.getUniqueId(), scoreboard);
        }
        return scoreboard;
    }

    private void checkScoreboardNotSet() {
        Require.checkState(updatingScoreboard == null, "This is only possible if updating scoreboard is not set");
    }

    private void checkScoreboardSet() {
        Require.checkState(updatingScoreboard != null, "This is only possible if updating scoreboard is set");
    }

}
