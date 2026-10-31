package me.noci.quickutilities.packethandler;

import com.google.common.collect.Maps;
import me.noci.quickutilities.hooks.ProtocolLibHook;
import me.noci.quickutilities.input.sign.packets.*;
import me.noci.quickutilities.quicktab.packets.*;

import java.util.HashMap;

public class PacketHandlerFactory {

    private static final HashMap<Class<PacketHandler<?>>, PacketHandlerContainer<?>> PACKET_HANDLER_MANAGERS = Maps.newHashMap();

    static {
        if (ProtocolLibHook.isEnabled()) {
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
    }

    public static <T extends PacketHandler<T>> T getPacketHandler(Class<T> type) {
        try {
            PacketHandlerContainer<T> packetManager = getPacketManager(type);
            return packetManager.handle();
        } catch (UnsupportedOperationException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to find packet handler", e);
        }
    }

    @SafeVarargs
    public static <T extends PacketHandler<T>> void registerPacketManger(boolean requireProtocolLib, Class<T> type, T... handlers) {
        if (requireProtocolLib && !ProtocolLibHook.isEnabled()) return;
        PacketHandlerContainer<T> manager = new PacketHandlerContainer<>(requireProtocolLib, type, handlers);
        registerPacketManger(manager);
    }

    @SuppressWarnings("unchecked")
    public static <T extends PacketHandler<T>> void registerPacketManger(PacketHandlerContainer<T> handlerManager) {
        if (PACKET_HANDLER_MANAGERS.containsKey(handlerManager.getType())) throw new IllegalStateException();
        PACKET_HANDLER_MANAGERS.put((Class<PacketHandler<?>>) handlerManager.getType(), handlerManager);
    }

    @SuppressWarnings("unchecked")
    private static <T extends PacketHandler<T>> PacketHandlerContainer<T> getPacketManager(Class<T> type) {
        if (PACKET_HANDLER_MANAGERS.containsKey(type))
            return (PacketHandlerContainer<T>) PACKET_HANDLER_MANAGERS.get(type);
        if (!ProtocolLibHook.isEnabled()) {
            throw new UnsupportedOperationException(
                    "%s of type '%s' currently only works with ProtocolLib.".formatted(
                            PacketHandler.class.getSimpleName(),
                            type.getName()
                    )
            );
        }
        throw new IllegalStateException("Could not find '%s' for %s of type '%s'.".formatted(PacketHandlerContainer.class.getSimpleName(), PacketHandler.class.getSimpleName(), type.getName()));
    }

}
