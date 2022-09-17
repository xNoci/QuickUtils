package me.noci.quickutilities.quicktab;

import com.google.common.collect.Sets;
import me.noci.quickutilities.quicktab.builder.DefaultTabListTeamBuilder;
import me.noci.quickutilities.quicktab.builder.TabListTeamBuilder;
import me.noci.quickutilities.quicktab.packets.TabListPacketManager;
import me.noci.quickutilities.quicktab.builder.TabListTeam;
import me.noci.quickutilities.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class QuickTab {

    private static UpdatingTabList updatingTabList = null;

    private QuickTab() {
        throw new UnsupportedOperationException();
    }

    /**
     * This method creates a new instance of a {@link TabListTeamBuilder}.
     *
     * @return {@link TabListTeamBuilder}
     */
    public static TabListTeamBuilder builder() {
        return new DefaultTabListTeamBuilder();
    }

    /**
     * Update the tab list of the specific player.
     *
     * @param player  The player whose tab list should be updated
     * @param builder The specific builder that should be used
     */
    public static void update(Player player, TabListTeamBuilder builder) {
        if (updatingTabList != null) {
            throw new IllegalStateException("This is only possible if no UpdatingTabList is set.");
        }
        internalUpdate(player, builder);
    }

    /**
     * Update the tab list of every online player of the server.
     *
     * @param builder The specific builder that should be used.
     */
    public static void updateAll(TabListTeamBuilder builder) {
        Bukkit.getOnlinePlayers().forEach(player -> update(player, builder));
    }

    public static void setUpdatingTabList(JavaPlugin plugin, TabListTeamBuilder builder) {
        if (updatingTabList != null) {
            throw new IllegalStateException("Cannot set updating tab list while one is already set.");
        }

        updatingTabList = new UpdatingTabList(plugin, builder);
        updatingTabList.update();
    }

    public static void removeUpdatingTabList() {
        if (updatingTabList == null) {
            throw new IllegalStateException("Cannot remove updating tab list, because no one is set.");
        }

        updatingTabList.delete();
        updatingTabList = null;
    }

    public static void update(Player player) {
        if (updatingTabList == null) {
            throw new IllegalStateException("This is only possible if an UpdatingTabList is set.");
        }
        updatingTabList.update(player);
    }

    public static void updateAll() {
        Bukkit.getOnlinePlayers().forEach(QuickTab::update);
    }

    protected static void internalUpdate(Player player, TabListTeamBuilder builder) {
        Set<Object> removePackets = Sets.newHashSet();
        Set<Object> createPackets = Sets.newHashSet();

        for (Player target : Bukkit.getOnlinePlayers()) {
            TabListTeam team = builder.build(player, target);

            removePackets.add(TabListPacketManager.removePacket(team));
            createPackets.add(TabListPacketManager.createPacket(team));
        }

        ReflectionUtils.sendPacketSync(player, removePackets.toArray());
        ReflectionUtils.sendPacketSync(player, createPackets.toArray());
    }

}
