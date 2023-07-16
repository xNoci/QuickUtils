package me.noci.quickutilities;

import me.noci.quickutilities.commands.QuickUtilsCommand;
import me.noci.quickutilities.input.sign.packets.SignPacketHandlerManager;
import me.noci.quickutilities.inventory.GuiManager;
import me.noci.quickutilities.listener.EntityDamageByEntityListener;
import me.noci.quickutilities.packethandler.PacketHandlerFactory;
import me.noci.quickutilities.quicktab.packets.TeamPacketHandlerManager;
import org.bukkit.plugin.java.JavaPlugin;

public class QuickUtils extends JavaPlugin {

    private static QuickUtils instance;

    public static QuickUtils instance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        PacketHandlerFactory.registerPacketManger(new TeamPacketHandlerManager());
        PacketHandlerFactory.registerPacketManger(new SignPacketHandlerManager());

        new GuiManager(this);

        registerListener();
        registerCommands();
    }

    private void registerListener() {
        new EntityDamageByEntityListener();
    }

    private void registerCommands() {
        new QuickUtilsCommand(this);
    }

}
