package me.noci.quickutilities.packethandler;

import com.cryptomorin.xseries.ReflectionUtils;
import me.noci.quickutilities.QuickUtils;
import me.noci.quickutilities.hooks.ProtocolLibHook;

import java.util.Set;

public interface PacketHandlerManager<T extends PacketHandler<T>> {

    boolean requiresProtocolLib();

    default T getHandler() {
        checkSupportedVersion(this);
        checkProtocolLib(this);
        return getHandlerInfo().getHandler();
    }

    PacketHandlerInfo<T> getHandlerInfo();

    Class<T> getHandlerType();

    default PacketHandlerInfo<T> findSupportedHandler(Set<PacketHandlerInfo<T>> handlers) {
        int currentVersion = ReflectionUtils.MINOR_NUMBER;
        PacketHandlerInfo<T> supportedPacket = handlers.stream().filter(packetInfo -> packetInfo.isSupported(currentVersion)).findFirst().orElse(PacketHandlerInfo.unknown());

        String info = "Using %s version mapping '%s'. Current server version: %s (%s)"
                .formatted(
                        getHandlerType().getSimpleName(),
                        supportedPacket.getHandler().protocolInfo(),
                        ReflectionUtils.NMS_VERSION,
                        ReflectionUtils.MINOR_NUMBER
                );

        QuickUtils.instance().getLogger().info(info);

        return supportedPacket;
    }

    private static <T extends PacketHandler<T>> void checkSupportedVersion(PacketHandlerManager<T> handlerManager) {
        if (!handlerManager.getHandlerInfo().isUnknownVersion()) return;
        throw new UnsupportedOperationException("%s of type '%s' currently does not support your version: %s (%s).".formatted(PacketHandler.class.getSimpleName(), handlerManager.getHandlerType().getName(), ReflectionUtils.NMS_VERSION, ReflectionUtils.MINOR_NUMBER));
    }

    private static <T extends PacketHandler<T>> void checkProtocolLib(PacketHandlerManager<T> handlerManager) {
        if (!handlerManager.requiresProtocolLib()) return;
        if (ProtocolLibHook.isEnabled()) return;
        throw new UnsupportedOperationException("%s of type '%s' currently only works with ProtocolLib.".formatted(PacketHandler.class.getSimpleName(), handlerManager.getHandlerType().getName()));
    }

}
