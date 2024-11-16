package me.noci.quickutilities.scoreboard;

import fr.mrmicky.fastboard.adventure.FastBoard;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class ComponentScoreboard implements Scoreboard<Component> {

    private final FastBoard fastBoard;

    public ComponentScoreboard(Player player) {
        this.fastBoard = new FastBoard(player);
    }

    @Override
    public Component getTitle() {
        return fastBoard.getTitle();
    }

    @Override
    public void updateTitle(Component title) {
        fastBoard.updateTitle(title);
    }

    @Override
    public List<Component> getLines() {
        return fastBoard.getLines();
    }

    @Override
    public Component getLine(int line) {
        return fastBoard.getLine(line);
    }

    @Override
    public void updateLine(int line, Component text) {
        fastBoard.updateLine(line, text);
    }

    @Override
    public void removeLine(int line) {
        fastBoard.removeLine(line);
    }

    @Override
    public void updateLines(Component... lines) {
        fastBoard.updateLines(lines);
    }

    @Override
    public void updateLines(Collection<Component> lines) {
        fastBoard.updateLines(lines);
    }

    @Override
    public Player getPlayer() {
        return fastBoard.getPlayer();
    }

    @Override
    public String getId() {
        return fastBoard.getId();
    }

    @Override
    public boolean isDeleted() {
        return fastBoard.isDeleted();
    }

    @Override
    public int size() {
        return fastBoard.size();
    }

    @Override
    public void delete() {
        fastBoard.delete();
    }
}
