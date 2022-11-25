package me.noci.quickutilities.utils;

import com.cryptomorin.xseries.XMaterial;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;

public class SkullItem {

    public static QuickItemStack getPlayerSkull(String playerName) {
        QuickItemStack skull = new QuickItemStack(XMaterial.PLAYER_HEAD.parseMaterial());
        skull.setSkullOwner(playerName);
        return skull;
    }

    public static QuickItemStack getSkull(String url) {
        QuickItemStack skull = new QuickItemStack(XMaterial.PLAYER_HEAD.parseMaterial());
        ItemMeta itemMeta = skull.getItemMeta();

        if (!url.startsWith("https://textures.minecraft.net/texture/")) {
            url = "https://textures.minecraft.net/texture/" + url;
        }

        GameProfile profile = getSkullProfile(url);

        try {
            Field field = itemMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(itemMeta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        skull.setItemMeta(itemMeta);

        return skull;
    }

    private static GameProfile getSkullProfile(String skinURL) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        String base64encoded = Base64.getEncoder().encodeToString(("{textures:{SKIN:{url:\"" + skinURL + "\"}}}").getBytes());
        Property property = new Property("textures", base64encoded);
        gameProfile.getProperties().put("textures", property);
        return gameProfile;
    }

}
