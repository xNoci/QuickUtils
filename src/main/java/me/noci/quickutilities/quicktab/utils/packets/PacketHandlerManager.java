package me.noci.quickutilities.quicktab.utils.packets;

import com.cryptomorin.xseries.ReflectionUtils;
import me.noci.quickutilities.QuickUtils;
import me.noci.quickutilities.utils.ProtocolLibHook;

import java.util.Set;

public interface PacketHandlerManager<T extends PacketHandler<T>> {

    boolean requiresProtocolLib();

    T handler();

    default T getHandler() {
        checkProtocolLib(this);
        return handler();
    }

    Class<T> getHandlerType();

    default T findSupportedHandle(Set<PacketHandleInfo<T>> handles, PacketHandleInfo<T> unknownVersion) {
        int currentVersion = ReflectionUtils.MINOR_NUMBER;
        PacketHandleInfo<T> supportedPacket = handles.stream().filter(packetInfo -> packetInfo.isSupported(currentVersion)).findFirst().orElse(unknownVersion);

        String info = "Using %s version mapping '%s'. Current server version: %s (%s)"
                .formatted(
                        getHandlerType().getSimpleName(),
                        supportedPacket.getHandle().protocolInfo(),
                        ReflectionUtils.NMS_VERSION,
                        ReflectionUtils.MINOR_NUMBER
                );

        QuickUtils.instance().getLogger().info(info);

        return supportedPacket.getHandle();
    }

    private static <T extends PacketHandler<T>> void checkProtocolLib(PacketHandlerManager<T> handlerManager) {
        if (!handlerManager.requiresProtocolLib()) return;
        if (ProtocolLibHook.isEnabled()) return;
        throw new UnsupportedOperationException("%s of type '%s' currently only works with ProtocolLib.".formatted(PacketHandler.class.getSimpleName(), handlerManager.getHandlerType().getName()));
    }

}
