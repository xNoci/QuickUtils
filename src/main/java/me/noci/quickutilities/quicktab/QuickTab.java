package me.noci.quickutilities.quicktab;

import com.cryptomorin.xseries.reflection.minecraft.MinecraftConnection;
import com.google.common.collect.Sets;
import me.noci.quickutilities.packethandler.PacketHandlerFactory;
import me.noci.quickutilities.quicktab.builder.DefaultQuickTabBuilder;
import me.noci.quickutilities.quicktab.builder.QuickTabBuilder;
import me.noci.quickutilities.quicktab.builder.TabListTeam;
import me.noci.quickutilities.quicktab.packets.TeamPacketHandler;
import me.noci.quickutilities.utils.BukkitUnit;
import me.noci.quickutilities.utils.Require;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import java.util.Set;

public class QuickTab {

    private static UpdatingTabList updatingTabList = null;

    private QuickTab() {
        throw new UnsupportedOperationException();
    }

    /**
     * This method creates a new instance of a {@link QuickTabBuilder}.
     *
     * @return {@link QuickTabBuilder}
     */
    public static QuickTabBuilder builder() {
        return new DefaultQuickTabBuilder();
    }

    /**
     * Update the tab list of the specific player.
     *
     * @param player  The player whose tab list should be updated
     * @param builder The specific builder that should be used
     * @throws IllegalStateException When a {@link UpdatingTabList} is set. Use {@link #update(Player)} instead or remove the {@link UpdatingTabList} via {@link #removeUpdatingTabList()}.
     */
    public static void update(Player player, QuickTabBuilder builder) {
        Require.checkState(updatingTabList == null, "This is only possible if no UpdatingTabList is set.");
        internalUpdate(player, builder);
    }

    /**
     * Update the tab list of every online player of the server.
     *
     * @param builder The specific builder that should be used
     * @throws IllegalStateException When a {@link UpdatingTabList} is set. Use {@link #updateAll()} instead or remove the {@link UpdatingTabList} via {@link #removeUpdatingTabList()}.
     */
    public static void updateAll(QuickTabBuilder builder) {
        Bukkit.getOnlinePlayers().forEach(player -> update(player, builder));
    }

    /**
     * Sets the {@link UpdatingTabList} which will automatically update the current used {@link QuickTabBuilder} when a player joins the server.
     *
     * @param builder The {@link QuickTabBuilder} which should be used
     * @throws IllegalStateException When a {@link UpdatingTabList} is already set. Use {@link #removeUpdatingTabList()} to remove the current one.
     */
    public static void setUpdatingTabList(QuickTabBuilder builder) {
        setUpdatingTabList(builder, -1, BukkitUnit.TICKS);
    }

    /**
     * Sets the {@link UpdatingTabList} which will automatically update the current used {@link QuickTabBuilder} when a player joins the server.
     * It also updates in a specific interval which can be set using {@param value} and {@param timeUnit}.
     *
     * @param builder  The {@link QuickTabBuilder} which should be used
     * @param value    interval to update scoreboard
     * @param timeUnit interval unit
     * @throws IllegalStateException When a {@link UpdatingTabList} is already set. Use {@link #removeUpdatingTabList()} to remove the current one.
     */
    public static void setUpdatingTabList(QuickTabBuilder builder, int value, BukkitUnit timeUnit) {
        Require.checkState(updatingTabList == null, "Cannot set updating tab list while one is already set.");
        updatingTabList = new UpdatingTabList(builder, value <= 0 ? -1 : timeUnit.toTicks(value));
        updatingTabList.updateAll();
    }

    /**
     * Removes the current used {@link UpdatingTabList}.
     *
     * @throws IllegalStateException When no {@link UpdatingTabList} is set.
     */
    public static void removeUpdatingTabList() {
        Require.checkState(updatingTabList != null, "Cannot remove updating tab list, because no one is set.");
        updatingTabList.delete();
        updatingTabList = null;
    }

    /**
     * Updates the tab list of the given {@link Player} using the {@link UpdatingTabList}.
     *
     * @throws IllegalStateException When no {@link UpdatingTabList} is set.
     */
    public static void update(Player player) {
        Require.checkState(updatingTabList != null, "This is only possible if an UpdatingTabList is set.");
        updatingTabList.update(player);
    }

    /**
     * Updates the tab list of every only {@link Player} using the {@link UpdatingTabList}.
     *
     * @throws IllegalStateException When no {@link UpdatingTabList} is set.
     */
    public static void updateAll() {
        Bukkit.getOnlinePlayers().forEach(QuickTab::update);
    }

    /**
     * This method is used internally to update the tab list of a {@link Player} using a {@link QuickTabBuilder}.
     * <br> It does not check whether a {@link UpdatingTabList} is currently set or not.
     *
     * @param player  The player which tab list should be updated
     * @param builder The builder that is used to update the tab list
     */
    @ApiStatus.Internal
    protected static void internalUpdate(Player player, QuickTabBuilder builder) {
        Set<Object> removePackets = Sets.newHashSet();
        Set<Object> createPackets = Sets.newHashSet();

        for (Player target : Bukkit.getOnlinePlayers()) {
            TabListTeam team = builder.build(player, target);

            TeamPacketHandler teamPacketHandler = PacketHandlerFactory.getPacketHandler(TeamPacketHandler.class);
            removePackets.add(teamPacketHandler.removeTeamPacket(team));
            createPackets.add(teamPacketHandler.createTeamPacket(team));
        }

        MinecraftConnection.sendPacket(player, removePackets.toArray());
        MinecraftConnection.sendPacket(player, createPackets.toArray());
    }

}
