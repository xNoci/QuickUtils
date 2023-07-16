package me.noci.quickutilities.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class StringScoreboard implements Scoreboard<String> {

    private final FastBoard fastBoard;

    public StringScoreboard(Player player) {
        this.fastBoard = new FastBoard(player);
    }

    @Override
    public String getTitle() {
        return fastBoard.getTitle();
    }

    @Override
    public void updateTitle(String title) {
        fastBoard.updateTitle(title);
    }

    @Override
    public List<String> getLines() {
        return fastBoard.getLines();
    }

    @Override
    public String getLine(int line) {
        return fastBoard.getLine(line);
    }

    @Override
    public void updateLine(int line, String text) {
        fastBoard.updateLine(line, text);
    }

    @Override
    public void removeLine(int line) {
        fastBoard.removeLine(line);
    }

    @Override
    public void updateLines(String... lines) {
        fastBoard.updateLines(lines);
    }

    @Override
    public void updateLines(Collection<String> lines) {
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
