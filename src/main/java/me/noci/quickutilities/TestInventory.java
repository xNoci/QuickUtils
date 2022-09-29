package me.noci.quickutilities;

import me.noci.quickutilities.inventory.GuiItem;
import me.noci.quickutilities.inventory.InventoryContent;
import me.noci.quickutilities.inventory.QuickGUIProvider;
import me.noci.quickutilities.utils.QuickItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Random;

public class TestInventory extends QuickGUIProvider {

    private static final GuiItem BORDER_ITEM = GuiItem.of(new QuickItemStack(Material.STAINED_GLASS_PANE, 1, 7).removeDisplayName());

    private static final GuiItem BTN_DETECTIVE_PASS = GuiItem.of(
            new QuickItemStack(Material.INK_SACK, 1, 12)
                    .setDisplayName("§9§lDetective")
                    .setLore("", "§7Passes: §einfinity", ""),
            event -> event.getPlayer().sendMessage("§f[§eTTT§f] §aYou successfully redeemed §eone detective pass §aand will be detective in this round."));

    private static final GuiItem BTN_INNOCENT_PASS = GuiItem.of(
            new QuickItemStack(Material.INK_SACK, 1, 10)
                    .setDisplayName("§a§lInnocent")
                    .setLore("", "§7Passes: §einfinity", ""),
            event -> event.getPlayer().sendMessage("§f[§eTTT§f] §aYou successfully redeemed §eone innocent pass §aand will be innocent in this round."));

    private static final GuiItem BTN_TRAITOR_PASS = GuiItem.of(
            new QuickItemStack(Material.INK_SACK, 1, 1)
                    .setDisplayName("§a§lTraitor")
                    .setLore("", "§7Passes: §einfinity", ""),
            event -> event.getPlayer().sendMessage("§f[§eTTT§f] §aYou successfully redeemed §eone traitor pass §aand will be traitor in this round."));

    private static final GuiItem BTN_START_ROUND = GuiItem.of(
            new QuickItemStack(Material.FEATHER)
                    .setDisplayName("§eStart round")
                    .setLore("", "§7Sets the time to 10 seconds.", "")
                    .glow(),
            event -> event.getPlayer().sendMessage("§f[§eTTT§f] §cTo accelerated the start of this game there need to be at least §e6 §cparticipants."));

    private int step = 0;

    protected TestInventory() {
        super("§cGame settings", 9 * 3);
    }

    protected TestInventory(Player player) {
        super("§cGame settings", 9 * 3);
    }

    @Override
    public void init(Player player, InventoryContent content) {
        content.fillBorders(BORDER_ITEM);

        content.setItem(11, BTN_DETECTIVE_PASS);
        content.setItem(12, BTN_INNOCENT_PASS);
        content.setItem(13, BTN_TRAITOR_PASS);
        content.setItem(15, BTN_START_ROUND);
        content.setItem(4, GuiItem.of(new QuickItemStack(Material.SKULL_ITEM, 1, 3).setSkullOwner(player.getName()).glow().setDisplayName("Cooler Head")));
    }

    @Override
    public void update(Player player, InventoryContent content) {
        if (step < 20) {
            step++;
            return;
        }
        step = 0;

        content.fillBorders(GuiItem.of(new QuickItemStack(Material.STAINED_GLASS_PANE, 1, new Random().nextInt(15)).removeDisplayName()));
        content.setItem(4, GuiItem.of(new QuickItemStack(Material.SKULL_ITEM, 1, 3).setSkullOwner(player.getName()).glow().setDisplayName("Cooler Head")));
    }
}
