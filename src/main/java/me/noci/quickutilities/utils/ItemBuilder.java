package me.noci.quickutilities.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import me.noci.quickutilities.inventory.ClickHandler;
import me.noci.quickutilities.inventory.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class ItemBuilder {

    private final Map<Class<? extends Property>, Property> properties = new IdentityHashMap<>();

    private ItemBuilder(Material material) {
        withMaterial(material);
    }

    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }

    public static ItemBuilder of(Material material, Component name) {
        return new ItemBuilder(material).withDisplayName(name);
    }

    public ItemStack build() {
        Optional<ItemMaterial> material = get(ItemMaterial.class);
        if (material.isEmpty()) {
            throw new IllegalStateException("Material not set");
        }

        ItemStack itemStack = material.get().createAsItem();
        return applyProperties(itemStack);
    }

    public GuiItem asGuiItem() {
        ItemStack item = build();
        return GuiItem.of(item);
    }

    public GuiItem asGuiItem(ClickHandler action) {
        ItemStack item = build();
        return GuiItem.of(item, action);
    }

    public ItemBuilder withMaterial(Material material) {
        return property(new ItemMaterial(material));
    }

    public ItemBuilder withAmount(int amount) {
        return property(new Amount(amount));
    }

    public ItemBuilder withLore(Component... lore) {
        return withLore(List.of(lore));
    }


    public ItemBuilder withLore(List<Component> lore) {
        return property(new Lore(lore));
    }

    @Deprecated
    public ItemBuilder withDisplayName(String displayName) {
        return withDisplayName(Legacy.deserialize(displayName));
    }

    public ItemBuilder withDisplayName(Component displayName) {
        return property(new DisplayName(displayName));
    }

    public ItemBuilder withItemFlags() {
        return withItemFlags(ItemFlag.values());
    }

    public ItemBuilder withItemFlags(ItemFlag... flags) {
        return property(new ItemFlags(flags));
    }

    public ItemBuilder withUnbreakable(boolean unbreakable) {
        return property(new Unbreakable(unbreakable));
    }

    public ItemBuilder withEnchantment(NamespacedKey key, int level) {
        final Registry<Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
        Enchantment enchantment = registry.getOrThrow(key);
        return property(new BookEnchantment(enchantment, level));
    }

    public ItemBuilder withEnchantment(TypedKey<Enchantment> key, int level) {
        final Registry<Enchantment> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
        Enchantment enchantment = registry.getOrThrow(key);
        return property(new BookEnchantment(enchantment, level));
    }

    public ItemBuilder withEnchantment(Enchantment enchantment, int level) {
        return property(new BookEnchantment(enchantment, level));
    }

    @Deprecated
    public ItemBuilder withBookTitle(String bookTitle) {
        return withBookTitle(Legacy.deserialize(bookTitle));
    }

    public ItemBuilder withBookTitle(Component bookTitle) {
        return property(new BookTitle(bookTitle));
    }

    @Deprecated
    public ItemBuilder withBookAuthor(String author) {
        return withBookAuthor(Legacy.deserialize(author));
    }

    public ItemBuilder withBookAuthor(Component author) {
        return property(new BookAuthor(author));
    }

    public ItemBuilder withBookPages(Component... pages) {
        return property(new BookPages(List.of(pages)));
    }

    public ItemBuilder withBookPages(List<Component> pages) {
        return property(new BookPages(pages));
    }

    public ItemBuilder withSkullOwner(PlayerProfile profile) {
        return property(new SkullOwner(profile));
    }

    public ItemBuilder withLeatherColor(Color color) {
        return property(new LeatherColor(color));
    }


    private ItemStack applyProperties(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        boolean changedItemMeta = false;

        for (Property value : properties.values()) {
            value.apply(itemStack, itemMeta);
            if (value.usesItemMeta() && !changedItemMeta) {
                changedItemMeta = true;
            }
        }

        if (changedItemMeta) {
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    @SuppressWarnings("unchecked")
    private <T extends Property> Optional<T> get(Class<T> property) {
        return Optional.ofNullable((T) properties.get(property));
    }

    private ItemBuilder property(Property property) {
        properties.put(property.getClass(), property);
        return this;
    }

    public interface Property {

        void apply(ItemStack item, ItemMeta itemMeta);

        boolean usesItemMeta();

    }

    private static class MetaProperty<M extends ItemMeta, T> implements Property {

        protected final T value;
        private final Class<M> metaClass;
        private final BiConsumer<M, T> applyFunction;

        private MetaProperty(T value, Class<M> metaClass, BiConsumer<M, T> applyFunction) {
            this.value = value;
            this.metaClass = metaClass;
            this.applyFunction = applyFunction;
        }

        @Override
        public void apply(ItemStack item, ItemMeta itemMeta) {
            if (!metaClass.isInstance(itemMeta)) return;
            M meta = metaClass.cast(itemMeta);
            this.applyFunction.accept(meta, value);
        }

        @Override
        public boolean usesItemMeta() {
            return true;
        }
    }

    private static class StackProperty<T> implements Property {

        protected final T value;
        private final BiConsumer<ItemStack, T> applyFunction;

        private StackProperty(T value, BiConsumer<ItemStack, T> applyFunction) {
            this.value = value;
            this.applyFunction = applyFunction;
        }

        @Override
        public void apply(ItemStack item, ItemMeta itemMeta) {
            this.applyFunction.accept(item, value);
        }

        @Override
        public boolean usesItemMeta() {
            return false;
        }
    }

    private static class ItemMaterial extends StackProperty<Material> {

        private ItemMaterial(@NotNull Material material) {
            super(material, (x, y) -> {
            });
        }

        public ItemStack createAsItem() {
            return new ItemStack(this.value);
        }
    }

    private static class Amount extends StackProperty<Integer> {

        private Amount(int amount) {
            super(amount, ItemStack::setAmount);
        }
    }

    private static class DisplayName extends MetaProperty<ItemMeta, Component> {
        private DisplayName(Component displayName) {
            super(displayName, ItemMeta.class, ItemMeta::displayName);
        }
    }


    private static class Lore extends MetaProperty<ItemMeta, List<Component>> {
        private Lore(List<Component> lore) {
            super(lore, ItemMeta.class, ItemMeta::lore);
        }
    }

    private static class ItemFlags extends MetaProperty<ItemMeta, ItemFlag[]> {

        private ItemFlags(ItemFlag[] flags) {
            super(flags, ItemMeta.class, ItemMeta::addItemFlags);
        }
    }

    private static class Unbreakable extends MetaProperty<ItemMeta, Boolean> {

        private Unbreakable(Boolean unbreakable) {
            super(unbreakable, ItemMeta.class, ItemMeta::setUnbreakable);
        }
    }

    private static class BookEnchantment extends MetaProperty<EnchantmentStorageMeta, Enchantment> {

        private BookEnchantment(Enchantment enchantment, int level) {
            super(enchantment, EnchantmentStorageMeta.class,
                    (itemMeta, enc) -> itemMeta.addStoredEnchant(enc, level, true));
        }
    }

    private static class BookTitle extends MetaProperty<BookMeta, Component> {

        private BookTitle(Component title) {
            super(title, BookMeta.class, BookMeta::title);
        }
    }

    private static class BookAuthor extends MetaProperty<BookMeta, Component> {

        private BookAuthor(Component title) {
            super(title, BookMeta.class, BookMeta::author);
        }
    }

    private static class BookPages extends MetaProperty<BookMeta, List<Component>> {
        private BookPages(List<Component> pages) {
            super(pages, BookMeta.class, BookMeta::pages);
        }
    }

    private static class SkullOwner extends MetaProperty<SkullMeta, PlayerProfile> {
        private SkullOwner(PlayerProfile profile) {
            super(profile, SkullMeta.class, SkullMeta::setPlayerProfile);
        }
    }

    private static class LeatherColor extends MetaProperty<LeatherArmorMeta, Color> {
        private LeatherColor(Color color) {
            super(color, LeatherArmorMeta.class, LeatherArmorMeta::setColor);
        }
    }


}
