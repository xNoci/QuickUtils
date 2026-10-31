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
    protected final void initialiseGui(Player player, GuiHolder holder) {
        super.initialiseGui(player, holder);

        if (holder instanceof PagedGuiHolder pagedGuiHolder) {
            initPage(player, pagedGuiHolder);
        }
    }

    @Override
    protected final void updateGui(Player player, GuiHolder holder) {
        super.updateGui(player, holder);
        if (holder instanceof PagedGuiHolder pagedGuiHolder) {
            updatePageContent(player, pagedGuiHolder);
        }
    }

    public abstract void initPage(Player player, InventoryPageContent pageContent);

    public abstract void updatePageContent(Player player, InventoryPageContent pageContent);

    @Override
    protected final GuiHolder holder() {
        return new PagedGuiHolder(this, this.type, this.size, this.title);
    }
}
