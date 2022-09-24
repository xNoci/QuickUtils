package me.noci.quickutilities;

import me.noci.quickutilities.inventory.InventoryContent;
import me.noci.quickutilities.inventory.InventoryProvider;
import me.noci.quickutilities.utils.QuickItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Random;

public class TestInventory extends InventoryProvider {

    private int step = 0;

    protected TestInventory() {
        super("§cGame settings", 9 * 3);
    }

    @Override
    public void init(Player player, InventoryContent content) {
        content.fillBorders(new QuickItemStack(Material.STAINED_GLASS_PANE, 1, 7).removeDisplayName());

        content.setItem(11, new QuickItemStack(Material.INK_SACK, 1, 12).setDisplayName("§9§lDetective").setLore("", "§7Passes: §einfinity", ""));
        content.setItem(12, new QuickItemStack(Material.INK_SACK, 1, 10).setDisplayName("§a§lInnocent").setLore("", "§7Passes: §einfinity", ""));
        content.setItem(13, new QuickItemStack(Material.INK_SACK, 1, 1).setDisplayName("§a§lTraitor").setLore("", "§7Passes: §einfinity", ""));
        content.setItem(15, new QuickItemStack(Material.FEATHER).setDisplayName("§eStart round").setLore("", "§7Sets the time to 10 seconds.", ""));
        content.setItem(4, new QuickItemStack(Material.SKULL_ITEM, 1, 3).setSkullOwner(player.getName()).glow().setDisplayName("Cooler Head"));

        content.setClickHandler(11, event -> event.getPlayer().sendMessage("§f[§eTTT§f] §aYou successfully redeemed §eone detective pass §aand will be detective in this round."));
        content.setClickHandler(12, event -> event.getPlayer().sendMessage("§f[§eTTT§f] §aYou successfully redeemed §eone innocent pass §aand will be innocent in this round."));
        content.setClickHandler(13, event -> event.getPlayer().sendMessage("§f[§eTTT§f] §aYou successfully redeemed §eone traitor pass §aand will be traitor in this round."));
        content.setClickHandler(15, event -> event.getPlayer().sendMessage("§f[§eTTT§f] §cTo accelerated the start of this game there need to be at least §e6 §cparticipants."));
    }

    @Override
    public void update(Player player, InventoryContent content) {
        if (step < 20) {
            step++;
            return;
        }
        step = 0;

        content.fillBorders(new QuickItemStack(Material.STAINED_GLASS_PANE, 1, new Random().nextInt(15)).removeDisplayName());
        content.setItem(4, new QuickItemStack(Material.SKULL_ITEM, 1, 3).setSkullOwner(player.getName()).glow().setDisplayName("Cooler Head"));
    }
}
