package me.noci.quickutilities.packethandler;

import com.cryptomorin.xseries.ReflectionUtils;
import me.noci.quickutilities.QuickUtils;
import me.noci.quickutilities.hooks.ProtocolLibHook;
import me.noci.quickutilities.utils.GenericType;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PacketHandlerManager<T extends PacketHandler<T>> extends GenericType<T> {

    private final PacketHandlerContainer<T> supportedContainer;
    private final boolean requiresProtocolLib;

    @SafeVarargs
    public PacketHandlerManager(boolean requiresProtocolLib, PacketHandler<T>... packetHandlers) {
        this.requiresProtocolLib = requiresProtocolLib;
        Set<PacketHandlerContainer<T>> handlers = Arrays.stream(packetHandlers).map(PacketHandler::createContainer).collect(Collectors.toSet());
        this.supportedContainer = findSupportedContainer(handlers);
    }

    public T getHandler() {
        checkSupportedVersion();
        checkProtocolLib();
        return packetContainer().getHandler();
    }

    public boolean requiresProtocolLib() {
        return this.requiresProtocolLib;
    }

    public PacketHandlerContainer<T> packetContainer() {
        return this.supportedContainer;
    }

    @SuppressWarnings({"unchecked", "UnstableApiUsage"})
    public Class<T> getHandlerType() {
        return (Class<T>) type.getRawType();
    }

    protected PacketHandlerContainer<T> findSupportedContainer(Set<PacketHandlerContainer<T>> handlers) {
        int currentVersion = ReflectionUtils.MINOR_NUMBER;
        PacketHandlerContainer<T> supportedPacket = handlers.stream().filter(packetInfo -> packetInfo.isSupported(currentVersion)).findFirst().orElse(PacketHandlerContainer.unknown());

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

    private void checkSupportedVersion() {
        if (!packetContainer().isUnknownVersion()) return;
        throw new UnsupportedOperationException("%s of type '%s' currently does not support your version: %s (%s).".formatted(PacketHandler.class.getSimpleName(), getHandlerType().getName(), ReflectionUtils.NMS_VERSION, ReflectionUtils.MINOR_NUMBER));
    }

    private void checkProtocolLib() {
        if (!requiresProtocolLib()) return;
        if (ProtocolLibHook.isEnabled()) return;
        throw new UnsupportedOperationException("%s of type '%s' currently only works with ProtocolLib.".formatted(PacketHandler.class.getSimpleName(), getHandlerType().getName()));
    }

}
