package me.noci.quickutilities.packethandler;

import com.google.common.collect.Maps;
import me.noci.quickutilities.input.sign.packets.SignPacketHandlerV1_18;
import me.noci.quickutilities.input.sign.packets.SignPacketHandlerV1_20;
import me.noci.quickutilities.input.sign.packets.SignPacketHandlerV1_8;
import me.noci.quickutilities.input.sign.packets.SignPacketHandlerV1_9;
import me.noci.quickutilities.quicktab.packets.TeamPacketHandlerV1_13;
import me.noci.quickutilities.quicktab.packets.TeamPacketHandlerV1_17;
import me.noci.quickutilities.quicktab.packets.TeamPacketHandlerV1_8;
import me.noci.quickutilities.quicktab.packets.TeamPacketHandlerV1_9;

import java.util.HashMap;

public class PacketHandlerFactory {

    private static final HashMap<Class<PacketHandler<?>>, PacketHandlerManager<?>> PACKET_HANDLER_MANAGERS = Maps.newHashMap();

    static {
        PacketHandlerFactory.registerPacketManger(true,
                new TeamPacketHandlerV1_8(),
                new TeamPacketHandlerV1_9(),
                new TeamPacketHandlerV1_13(),
                new TeamPacketHandlerV1_17()
        );

        PacketHandlerFactory.registerPacketManger(true,
                new SignPacketHandlerV1_8(),
                new SignPacketHandlerV1_9(),
                new SignPacketHandlerV1_18(),
                new SignPacketHandlerV1_20()
        );
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

    @SafeVarargs
    public static <T extends PacketHandler<T>> void registerPacketManger(boolean requireProtocolLib, PacketHandler<T>... handlers) {
        PacketHandlerManager<T> manager = new PacketHandlerManager<>(requireProtocolLib, handlers);
        registerPacketManger(manager);
    }

    @SuppressWarnings("unchecked")
    public static <T extends PacketHandler<T>> void registerPacketManger(PacketHandlerManager<T> handlerManager) {
        if (PACKET_HANDLER_MANAGERS.containsKey(handlerManager.getHandlerType())) throw new IllegalStateException();
        PACKET_HANDLER_MANAGERS.put((Class<PacketHandler<?>>) handlerManager.getHandlerType(), handlerManager);
    }

    @SuppressWarnings("unchecked")
    private static <T extends PacketHandler<T>> PacketHandlerManager<T> getPacketManager(Class<T> type) {
        if (PACKET_HANDLER_MANAGERS.containsKey(type))
            return (PacketHandlerManager<T>) PACKET_HANDLER_MANAGERS.get(type);
        throw new IllegalStateException("Could not find '%s' for %s of type '%s'.".formatted(PacketHandlerManager.class.getSimpleName(), PacketHandler.class.getSimpleName(), type.getName()));
    }

}
