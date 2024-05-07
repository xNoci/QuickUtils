package me.noci.quickutilities.inventory;

import com.google.common.base.Preconditions;
import lombok.Getter;
import me.noci.quickutilities.utils.Legacy;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

@Getter
public abstract class QuickGUIProvider implements GuiProvider {

    final Component title;
    final InventoryType type;
    final int size;

    protected QuickGUIProvider(int size) {
        this(InventoryType.CHEST.defaultTitle(), size);
    }

    protected QuickGUIProvider(InventoryType type) {
        this(type, type.defaultTitle());
    }

    @Deprecated
    protected QuickGUIProvider(String title, int size) {
        this(InventoryType.CHEST, Legacy.deserialize(title), size);
    }

    @Deprecated
    protected QuickGUIProvider(InventoryType type, String title) {
        this(type, Legacy.deserialize(title), type.getDefaultSize());
    }

    protected QuickGUIProvider(Component title, int size) {
        this(InventoryType.CHEST, title, size);
    }

    protected QuickGUIProvider(InventoryType type, Component title) {
        this(type, title, type.getDefaultSize());
    }

    QuickGUIProvider(InventoryType type, Component title, int size) {
        Preconditions.checkNotNull(type, "InventoryType cannot be null");
        Preconditions.checkNotNull(title, "Title cannot be null");

        if (type == InventoryType.CHEST) {
            Preconditions.checkArgument(size % 9 == 0 && size >= 9 && size <= 54, "Size for custom inventory must be a multiple of 9 between 9 and 54 slots (got %s)".formatted(size));
        }

        this.type = type;
        this.title = title;
        this.size = size;
    }

    @Override
    public void provide(Player player) {
        GuiHolder inventoryHolder = new GuiHolder(this, this.type, this.size, this.title);

        initialiseGui(player, inventoryHolder);
        inventoryHolder.applyContent();

        player.openInventory(inventoryHolder.getInventory());
    }

    protected void initialiseGui(Player player, GuiHolder holder) {
        init(player, holder.getContent());
    }

    public boolean isCancelledClick() {
        return true;
    }

    public void update(Player player, InventoryContent content) {
    }

    public abstract void init(Player player, InventoryContent content);


}
