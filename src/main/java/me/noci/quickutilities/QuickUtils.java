package me.noci.quickutilities;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.noci.quickutilities.commands.QuickUtilsCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class QuickUtils extends JavaPlugin {

    private static QuickUtils instance;

    public static QuickUtils instance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
    }
}
