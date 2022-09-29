package me.noci.quickutilities;

import me.noci.quickutilities.inventory.GuiItem;
import me.noci.quickutilities.inventory.InventoryContent;
import me.noci.quickutilities.inventory.PageContent;
import me.noci.quickutilities.inventory.PagedQuickGUIProvider;
import me.noci.quickutilities.utils.InventoryPattern;
import me.noci.quickutilities.utils.QuickItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Random;

public class PageTestInventory extends PagedQuickGUIProvider {

    protected PageTestInventory() {
        super("§cPage Test", 9 * 5);
    }

    @Override
    public void initPage(Player player, PageContent pageContent) {
        pageContent.setItemSlots(InventoryPattern.CENTER.getSlots(getType(), getSize()));
        pageContent.setPreviousPageItem(37, new QuickItemStack(Material.ARROW).setStackSize(1).glow().setDisplayName("Previous"), new QuickItemStack(Material.STAINED_GLASS_PANE, 1, 7).removeDisplayName());
        pageContent.setNextPageItem(43, new QuickItemStack(Material.ARROW).setStackSize(2).glow().setDisplayName("Next"), new QuickItemStack(Material.STAINED_GLASS_PANE, 1, 7).removeDisplayName());

        GuiItem[] content = new GuiItem[150];

        for (int i = 0; i < content.length; i++) {
            final int n = i;
            Material mat;
            do {
                int pick = new Random().nextInt(Material.values().length);
                mat = Material.values()[pick];
            } while (mat.isBlock());
            content[i] = GuiItem.of(new QuickItemStack(mat).setStackSize(n + 1).setDisplayName("Dirt " + n), event -> event.getPlayer().sendMessage("Clicked dirt " + n));
        }

        pageContent.setPageContent(content);
    }

    @Override
    public void init(Player player, InventoryContent content) {
        content.fill(GuiItem.of(new QuickItemStack(Material.STAINED_GLASS_PANE, 1, 7).removeDisplayName()));
    }
}
