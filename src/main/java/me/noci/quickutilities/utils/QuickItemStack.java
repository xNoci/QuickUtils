package me.noci.quickutilities.utils;

import com.google.common.collect.Lists;
import me.noci.quickutilities.inventory.ClickHandler;
import me.noci.quickutilities.inventory.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QuickItemStack extends ItemStack {

    public QuickItemStack(ItemStack itemStack) {
        super(itemStack);
    }

    public QuickItemStack(Material material) {
        super(material);
    }

    public QuickItemStack(Material material, Component component) {
        this(material, Legacy.SERIALIZER.serialize(component));
    }

    public QuickItemStack(Material material, String displayName) {
        super(material, 1);
        setDisplayName(displayName);
    }

    public QuickItemStack(Material material, int amount) {
        super(material, amount);
    }

    public QuickItemStack(Material material, int amount, int subId) {
        super(material, amount, (short) subId);
    }

    public static QuickItemStack fromBase64(String base64) {
        try {
            return new QuickItemStack(ItemSerializer.itemStackFromBase64(base64));
        } catch (IOException e) {
            throw new RuntimeException("Unable to decode class type.", e);
        }
    }

    public String toBase64() {
        return ItemSerializer.itemStackToBase64(this);
    }

    public QuickItemStack displayName(Component component) {
        return setDisplayName(Legacy.serialize(component));
    }

    public String getDisplayName() {
        if (this.getItemMeta() == null) return getType().name();
        return this.getItemMeta().getDisplayName();
    }

    public QuickItemStack setDisplayName(String name) {
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setDisplayName(name);
        this.setItemMeta(itemMeta);
        return this;
    }

    public QuickItemStack setStackSize(int amount) {
        this.setAmount(amount);
        return this;
    }

    public QuickItemStack removeDisplayName() {
        return setDisplayName(ChatColor.RESET.toString());
    }

    public QuickItemStack setMeta(ItemMeta itemMeta) {
        this.setItemMeta(itemMeta);
        return this;
    }

    public QuickItemStack itemLore(Component... lore) {
        return itemLore(Arrays.asList(lore));
    }

    public QuickItemStack itemLore(List<Component> lore) {
        return setItemLore(lore.stream().map(Legacy::serialize).collect(Collectors.toList()));
    }

    @Deprecated
    public QuickItemStack itemLore(String... lore) {
        return setItemLore(Arrays.asList(lore));
    }

    @Deprecated
    public QuickItemStack setItemLore(List<String> lore) {
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setLore(lore);
        this.setItemMeta(itemMeta);
        return this;
    }

    public QuickItemStack addEnchantment(Enchantment enchantment, long level) {
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.addEnchant(enchantment, (int) level, false);
        this.setItemMeta(itemMeta);
        return this;
    }

    public QuickItemStack addItemFlags() {
        return addItemFlag(ItemFlag.values());
    }

    public QuickItemStack addItemFlag(ItemFlag... itemFlags) {
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.addItemFlags(itemFlags);
        this.setItemMeta(itemMeta);
        return this;
    }

    public QuickItemStack setUnbreakable(boolean unbreakable) {
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setUnbreakable(unbreakable);
        this.setItemMeta(itemMeta);
        return this;
    }

    @Override
    @Deprecated
    public void addEnchantment(Enchantment enchantment, int level) {
        addUnsafeEnchantment(enchantment, level);
    }

    public QuickItemStack setUnsafeEnchantment(Enchantment ench, long level) {
        addUnsafeEnchantment(ench, (int) level);
        return this;
    }


    public QuickItemStack setSkullOwner(String name) {
        SkullMeta skullMeta = (SkullMeta) this.getItemMeta();
        skullMeta.setOwner(name);
        this.setItemMeta(skullMeta);
        return this;
    }

    public QuickItemStack bookTitle(Component component) {
        return setTitle(Legacy.serialize(component));
    }

    public QuickItemStack setTitle(String title) {
        BookMeta bookMeta = (BookMeta) this.getItemMeta();
        bookMeta.setTitle(title);
        this.setItemMeta(bookMeta);
        return this;
    }

    public QuickItemStack addBookPages(Component... pages) {
        return addBookPages(Arrays.asList(pages));
    }

    public QuickItemStack addBookPages(List<Component> pages) {
        return addPage(pages.stream().map(Legacy::serialize).collect(Collectors.toList()));
    }

    public QuickItemStack addPage(List<String> pages) {
        return addPage(pages.toArray(String[]::new));
    }

    public QuickItemStack addPage(String... pages) {
        BookMeta bookMeta = (BookMeta) this.getItemMeta();
        bookMeta.addPage(pages);
        this.setItemMeta(bookMeta);
        return this;
    }

    public QuickItemStack bookPages(Component... pages) {
        return bookPages(Arrays.asList(pages));
    }

    public QuickItemStack bookPages(List<Component> pages) {
        return setPages(pages.stream().map(Legacy::serialize).collect(Collectors.toList()));
    }

    public QuickItemStack setPages(List<String> pages) {
        BookMeta bookMeta = (BookMeta) this.getItemMeta();
        bookMeta.setPages(pages);
        this.setItemMeta(bookMeta);
        return this;
    }

    public QuickItemStack setPages(String... pages) {
        BookMeta bookMeta = (BookMeta) this.getItemMeta();
        bookMeta.setPages(pages);
        this.setItemMeta(bookMeta);
        return this;
    }

    public QuickItemStack bookAuthor(Component author) {
        return setAuthor(Legacy.serialize(author));
    }

    public QuickItemStack setAuthor(String author) {
        BookMeta bookMeta = (BookMeta) this.getItemMeta();
        bookMeta.setAuthor(author);
        this.setItemMeta(bookMeta);
        return this;
    }

    public QuickItemStack setBookEnchantment(Enchantment enchantment, int level) {
        EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) this.getItemMeta();
        enchantmentStorageMeta.addStoredEnchant(enchantment, level, true);
        this.setItemMeta(enchantmentStorageMeta);
        return this;
    }

    public QuickItemStack setLeatherColor(Color color) {
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) this.getItemMeta();
        leatherArmorMeta.setColor(color);
        this.setItemMeta(leatherArmorMeta);
        return this;
    }

    public QuickItemStack glow() {
        return this.glow(false);
    }

    public QuickItemStack glow(boolean remove) {
        addItemFlags();
        if (!remove) {
            setUnsafeEnchantment(Enchantment.UNBREAKING, 1);
        } else {
            removeEnchantment(Enchantment.UNBREAKING);
        }
        return this;
    }

    public GuiItem asGuiItem() {
        return GuiItem.of(this);
    }

    public GuiItem asGuiItem(ClickHandler action) {
        return GuiItem.of(this, action);
    }

    public String getRawDisplayName() {
        return ChatColor.stripColor(getDisplayName());
    }

}
