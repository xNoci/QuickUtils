package me.noci.quickutilities.packethandler;

import me.noci.quickutilities.input.sign.packets.SignPacketHandler;
import me.noci.quickutilities.input.sign.packets.SignPacketHandlerManager;
import me.noci.quickutilities.quicktab.packets.TeamPacketHandler;
import me.noci.quickutilities.quicktab.packets.TeamPacketHandlerManager;

public class PacketHandlerFactory {

    private static final TeamPacketHandlerManager TEAM_PACKET_MANAGER = new TeamPacketHandlerManager();
    private static final SignPacketHandlerManager SIGN_PACKET_MANAGER = new SignPacketHandlerManager();

    public static <T extends PacketHandler<T>> T getPacketHandler(Class<T> type) {
        try {
            PacketHandlerManager<T> packetManager = getPacketManager(type);
            return packetManager.getHandler();
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    private static <T extends PacketHandler<T>> PacketHandlerManager<T> getPacketManager(Class<T> type) {
        if (type.equals(TeamPacketHandler.class)) return (PacketHandlerManager<T>) TEAM_PACKET_MANAGER;
        if (type.equals(SignPacketHandler.class)) return (PacketHandlerManager<T>) SIGN_PACKET_MANAGER;

        throw new IllegalStateException("Could not find '%s' for %s of type '%s'.".formatted(PacketHandlerManager.class.getSimpleName(), PacketHandler.class.getSimpleName(), type.getName()));
    }


}
