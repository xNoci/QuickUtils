package me.noci.quickutilities.hooks.viaversion;

import com.cryptomorin.xseries.reflection.XReflection;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import me.noci.quickutilities.hooks.PluginHook;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ViaVersionHook extends PluginHook {

    protected ViaVersionHook() {
        super("ViaVersion");
    }

    public static boolean isEnabled() {
        return Singleton.HOOK.enabled();
    }

    public static int getPlayerVersionProtocol(UUID uuid) {
        if (!isEnabled()) return -1;
        return api().getPlayerVersion(uuid);
    }

    public static MinecraftVersion getVersion(Player player) {
        if (!isEnabled())
            return new MinecraftVersion(XReflection.MINOR_NUMBER, XReflection.getLatestPatchNumberOf(XReflection.MINOR_NUMBER), false);
        return Version.getVersion(player);
    }

    private static ViaAPI api() {
        return Via.getAPI();
    }

    private static class Singleton {
        private static final ViaVersionHook HOOK = new ViaVersionHook();
    }

}
