package me.noci.quickutilities.inventory;

@FunctionalInterface
public interface ClickHandler {

    ClickHandler DEFAULT = event -> {};

    void handle(SlotClickEvent event);

}
