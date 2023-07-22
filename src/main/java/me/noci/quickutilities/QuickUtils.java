package me.noci.quickutilities;

import me.noci.quickutilities.commands.QuickUtilsCommand;
import me.noci.quickutilities.inventory.GuiManager;
import me.noci.quickutilities.listener.EntityDamageByEntityListener;
import org.bukkit.plugin.java.JavaPlugin;

public class QuickUtils extends JavaPlugin {

    private static QuickUtils instance;

    public static QuickUtils instance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;


        new GuiManager(this);

        registerListener();
        registerCommands();
    }

    private void registerListener() {
        EntityDamageByEntityListener.initialise();
    }

    private void registerCommands() {
        new QuickUtilsCommand(this);
    }

}
