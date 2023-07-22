package me.noci.quickutilities.packethandler;

import com.google.common.collect.Maps;
import me.noci.quickutilities.input.sign.packets.SignPacketHandlerManager;
import me.noci.quickutilities.quicktab.packets.TeamPacketHandlerManager;

import java.util.HashMap;

public class PacketHandlerFactory<T extends PacketHandler<T>> {

    private static final HashMap<Class<PacketHandler<?>>, PacketHandlerManager<?>> PACKET_HANDLER_MANAGERS = Maps.newHashMap();

    static {
        PacketHandlerFactory.registerPacketManger(new TeamPacketHandlerManager());
        PacketHandlerFactory.registerPacketManger(new SignPacketHandlerManager());
    }

    public static <T extends PacketHandler<T>> T getPacketHandler(Class<T> type) {
        try {
            PacketHandlerManager<T> packetManager = getPacketManager(type);
            return packetManager.getHandler();
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends PacketHandler<T>> void registerPacketManger(PacketHandlerManager<T> handlerManager) {
        if (PACKET_HANDLER_MANAGERS.containsKey(handlerManager.getHandlerType())) throw new IllegalStateException();
        PACKET_HANDLER_MANAGERS.put((Class<PacketHandler<?>>) handlerManager.getHandlerType(), handlerManager);
    }

    @SuppressWarnings("unchecked")
    private static <T extends PacketHandler<T>> PacketHandlerManager<T> getPacketManager(Class<T> type) {
        if (PACKET_HANDLER_MANAGERS.containsKey(type)) return (PacketHandlerManager<T>) PACKET_HANDLER_MANAGERS.get(type);
        throw new IllegalStateException("Could not find '%s' for %s of type '%s'.".formatted(PacketHandlerManager.class.getSimpleName(), PacketHandler.class.getSimpleName(), type.getName()));
    }

}
