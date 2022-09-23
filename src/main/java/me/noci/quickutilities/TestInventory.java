package me.noci.quickutilities;

import me.noci.quickutilities.inventory.InventoryContent;
import me.noci.quickutilities.inventory.InventoryProvider;
import me.noci.quickutilities.utils.QuickItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Random;

public class TestInventory extends InventoryProvider {

    protected TestInventory() {
        super("§7Test", 9);
    }

    @Override
    public void init(Player player, InventoryContent content) {
        content.setItem(0, new QuickItemStack(Material.DIRT).glow().setDisplayName("Cooler Dirt :D"));
        content.setItem(1, new QuickItemStack(Material.SKULL_ITEM, 1, 3).setSkullOwner(player.getName()).glow().setDisplayName("Cooler Head"));
    }

    @Override
    public void update(Player player, InventoryContent content) {
        content.setItem(3, new QuickItemStack(Material.STAINED_GLASS_PANE, 1, new Random().nextInt(15)).glow().removeDisplayName());
    }
}
