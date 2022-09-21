package me.noci.quickutilities.quickgui;

import me.noci.quickutilities.events.gui.GuiClickEvent;

@FunctionalInterface
public interface ClickHandler {

    void handle(GuiClickEvent event);

}
