package me.noci.quickutilities.inventory;

import me.noci.quickutilities.utils.Legacy;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public abstract class PagedQuickGUIProvider extends QuickGUIProvider {

    protected PagedQuickGUIProvider(int size) {
        super(InventoryType.CHEST.defaultTitle(), size);
    }

    protected PagedQuickGUIProvider(InventoryType type) {
        this(type, type.defaultTitle());
    }

    @Deprecated
    protected PagedQuickGUIProvider(String title, int size) {
        this(InventoryType.CHEST, Legacy.deserialize(title), size);
    }

    @Deprecated
    protected PagedQuickGUIProvider(InventoryType type, String title) {
        this(type, Legacy.deserialize(title), type.getDefaultSize());
    }

    protected PagedQuickGUIProvider(Component title, int size) {
        this(InventoryType.CHEST, title, size);
    }

    protected PagedQuickGUIProvider(InventoryType type, Component title) {
        this(type, title, type.getDefaultSize());
    }

    PagedQuickGUIProvider(InventoryType type, Component title, int size) {
        super(type, title, size);
    }

    @Override
    protected void initialiseGui(Player player, GuiHolder holder) {
        super.initialiseGui(player, holder);

        PageContent pageContent = new PageContent();
        holder.setPageContent(pageContent);
        initPage(player, pageContent);
    }

    public abstract void initPage(Player player, PageContent pageContent);

    public void updatePageContent(Player player, PageContent pageContent) {
    }

}
