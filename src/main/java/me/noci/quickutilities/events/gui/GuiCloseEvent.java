package me.noci.quickutilities.events.gui;

import lombok.Getter;
import me.noci.quickutilities.events.core.CorePlayerEvent;
import me.noci.quickutilities.quickgui.QuickGUI;
import org.bukkit.entity.Player;

/**
 * This event will be called when a player closes a {@link QuickGUI} inventory.
 */
public class GuiCloseEvent extends CorePlayerEvent {

    /**
     * The {@link QuickGUI} that was closed.
     */
    @Getter private final QuickGUI gui;

    public GuiCloseEvent(Player player, QuickGUI gui) {
        super(player);
        this.gui = gui;
    }

}
