package me.noci.quickutilities.packethandler;

import com.google.common.collect.Maps;
import me.noci.quickutilities.input.sign.packets.*;
import me.noci.quickutilities.quicktab.packets.*;

import java.util.HashMap;

public class PacketHandlerFactory {

    private static final HashMap<Class<PacketHandler<?>>, PacketHandlerManager<?>> PACKET_HANDLER_MANAGERS = Maps.newHashMap();

    static {
        PacketHandlerFactory.registerPacketManger(
                true,
                TeamPacketHandler.class,
                new TeamPacketHandlerV1_8(),
                new TeamPacketHandlerV1_9(),
                new TeamPacketHandlerV1_13(),
                new TeamPacketHandlerV1_17(),
                new TeamPacketHandlerV1_20_4()
        );

        PacketHandlerFactory.registerPacketManger(
                true,
                SignPacketHandler.class,
                new SignPacketHandlerV1_8(),
                new SignPacketHandlerV1_9(),
                new SignPacketHandlerV1_18(),
                new SignPacketHandlerV1_20()
        );
    }

    public static <T extends PacketHandler<T>> T getPacketHandler(Class<T> type) {
        try {
            PacketHandlerManager<T> packetManager = getPacketManager(type);
            return packetManager.handle();
        } catch (Exception e) {
            e.fillInStackTrace();
            throw e;
        }
    }

    @SafeVarargs
    public static <T extends PacketHandler<T>> void registerPacketManger(boolean requireProtocolLib, Class<T> type, T... handlers) {
        PacketHandlerManager<T> manager = new PacketHandlerManager<>(requireProtocolLib, type, handlers);
        registerPacketManger(manager);
    }

    @SuppressWarnings("unchecked")
    public static <T extends PacketHandler<T>> void registerPacketManger(PacketHandlerManager<T> handlerManager) {
        if (PACKET_HANDLER_MANAGERS.containsKey(handlerManager.getType())) throw new IllegalStateException();
        PACKET_HANDLER_MANAGERS.put((Class<PacketHandler<?>>) handlerManager.getType(), handlerManager);
    }

    @SuppressWarnings("unchecked")
    private static <T extends PacketHandler<T>> PacketHandlerManager<T> getPacketManager(Class<T> type) {
        if (PACKET_HANDLER_MANAGERS.containsKey(type))
            return (PacketHandlerManager<T>) PACKET_HANDLER_MANAGERS.get(type);
        throw new IllegalStateException("Could not find '%s' for %s of type '%s'.".formatted(PacketHandlerManager.class.getSimpleName(), PacketHandler.class.getSimpleName(), type.getName()));
    }

}
