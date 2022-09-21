package me.noci.quickutilities.events.gui;

import lombok.Getter;
import me.noci.quickutilities.events.core.CorePlayerEvent;
import me.noci.quickutilities.quickgui.QuickGUI;
import org.bukkit.entity.Player;

/**
 * This event will be called when a player opens a {@link QuickGUI} inventory.
 */
public class GuiOpenEvent extends CorePlayerEvent {

    /**
     * The {@link QuickGUI} that was opened.
     */
    @Getter private final QuickGUI gui;

    public GuiOpenEvent(Player player, QuickGUI gui) {
        super(player);
        this.gui = gui;
    }

}
