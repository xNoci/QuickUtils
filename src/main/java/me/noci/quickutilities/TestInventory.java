package me.noci.quickutilities;

import me.noci.quickutilities.inventory.InventoryContent;
import me.noci.quickutilities.inventory.InventoryProvider;
import me.noci.quickutilities.utils.QuickItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Random;

public class TestInventory extends InventoryProvider {

    protected TestInventory() {
        super("§7Test", 9 * 4);
    }

    @Override
    public void init(Player player, InventoryContent content) {
        content.setItem(10, new QuickItemStack(Material.DIRT).glow().setDisplayName("Cooler Dirt :D"));
        content.setItem(13, new QuickItemStack(Material.SKULL_ITEM, 1, 3).setSkullOwner(player.getName()).glow().setDisplayName("Cooler Head"));

        content.setClickHandler(13, user -> user.sendMessage("Hey"));
    }

    private int step = 0;

    @Override
    public void update(Player player, InventoryContent content) {

        if (step < 5) {
            step++;
            return;
        }

        step = 0;

        content.fillBorders(new QuickItemStack(Material.STAINED_GLASS_PANE, 1, new Random().nextInt(15)).removeDisplayName());
    }
}
