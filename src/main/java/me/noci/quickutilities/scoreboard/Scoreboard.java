package me.noci.quickutilities.scoreboard;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public interface Scoreboard<T> {

    /**
     * Get the scoreboard title.
     *
     * @return the scoreboard title
     */
    T getTitle();

    /**
     * Update the scoreboard title.
     *
     * @param title the new scoreboard title
     * @throws IllegalArgumentException if the title is longer than 32 chars on 1.12 or lower
     * @throws IllegalStateException    if {@link #delete()} was call before
     */
    void updateTitle(T title);

    /**
     * Get the scoreboard lines.
     *
     * @return the scoreboard lines
     */
    List<T> getLines();

    /**
     * Get the specified scoreboard line.
     *
     * @param line the line number
     * @return the line
     * @throws IndexOutOfBoundsException if the line is higher than {@code size}
     */
    T getLine(int line);

    /**
     * Update a single scoreboard line.
     *
     * @param line the line number
     * @param text the new line text
     * @throws IndexOutOfBoundsException if the line is higher than {@link #size() size() + 1}
     */
    void updateLine(int line, T text);

    /**
     * Remove a scoreboard line.
     *
     * @param line the line number
     */
    void removeLine(int line);

    /**
     * Update all the scoreboard lines.
     *
     * @param lines the new lines
     * @throws IllegalArgumentException if one line is longer than 30 chars on 1.12 or lower
     * @throws IllegalStateException    if {@link #delete()} was call before
     */
    void updateLines(T... lines);

    /**
     * Update the lines of the scoreboard
     *
     * @param lines the new scoreboard lines
     * @throws IllegalArgumentException if one line is longer than 30 chars on 1.12 or lower
     * @throws IllegalStateException    if {@link #delete()} was call before
     */
    void updateLines(Collection<T> lines);

    /**
     * Get the player who has the scoreboard.
     *
     * @return current player for this FastBoard
     */
    Player getPlayer();

    /**
     * Get the scoreboard id.
     *
     * @return the id
     */
    String getId();

    /**
     * Get if the scoreboard is deleted.
     *
     * @return true if the scoreboard is deleted
     */
    boolean isDeleted();

    /**
     * Get the scoreboard size (the number of lines).
     *
     * @return the size
     */
    int size();

    /**
     * Delete this FastBoard, and will remove the scoreboard for the associated player if he is online.
     * After this, all uses of {@link #updateLines} and {@link #updateTitle} will throw an {@link IllegalStateException}
     *
     * @throws IllegalStateException if this was already call before
     */
    void delete();

}
