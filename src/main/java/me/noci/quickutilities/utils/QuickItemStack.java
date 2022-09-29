package me.noci.quickutilities.utils;

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

public class QuickItemStack extends ItemStack {

    public QuickItemStack(ItemStack itemStack) {
        super(itemStack);
    }

    public QuickItemStack(Material material) {
        super(material);
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

    public String getDisplayName() {
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

    public QuickItemStack setLore(List<String> lore) {
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setLore(lore);
        this.setItemMeta(itemMeta);
        return this;
    }

    public QuickItemStack setLore(String... lore) {
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setLore(Arrays.asList(lore));
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
        itemMeta.spigot().setUnbreakable(unbreakable);
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

    public QuickItemStack setTitle(String title) {
        BookMeta bookMeta = (BookMeta) this.getItemMeta();
        bookMeta.setTitle(title);
        this.setItemMeta(bookMeta);
        return this;
    }

    public QuickItemStack addPage(List<String> pageContent) {
        BookMeta bookMeta = (BookMeta) this.getItemMeta();
        bookMeta.addPage(pageContent.toString());
        this.setItemMeta(bookMeta);
        return this;
    }

    public QuickItemStack setAuthor(String author) {
        BookMeta bookMeta = (BookMeta) this.getItemMeta();
        bookMeta.setAuthor(author);
        this.setItemMeta(bookMeta);
        return this;
    }

    public QuickItemStack addPage(String... pageContent) {
        BookMeta bookMeta = (BookMeta) this.getItemMeta();
        bookMeta.addPage(pageContent);
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
        addItemFlags();
        setUnsafeEnchantment(Enchantment.DURABILITY, 1);
        return this;
    }

    public String getRawDisplayName() {
        return ChatColor.stripColor(getDisplayName());
    }

    public boolean isNull() {
        return getType() == null || getType() == Material.AIR;
    }

}
