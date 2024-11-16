package me.noci.quickutilities.packethandler;

import com.cryptomorin.xseries.reflection.XReflection;
import lombok.Getter;
import me.noci.quickutilities.QuickUtils;
import me.noci.quickutilities.hooks.ProtocolLibHook;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PacketHandlerManager<T extends PacketHandler<T>> {

    @Getter private final Class<T> type;
    private final PacketHandlerContainer<T> supportedContainer;
    private final boolean requiresProtocolLib;

    @SafeVarargs
    public PacketHandlerManager(boolean requiresProtocolLib, Class<T> type, PacketHandler<T>... packetHandlers) {
        this.requiresProtocolLib = requiresProtocolLib;
        this.type = type;
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

    protected PacketHandlerContainer<T> findSupportedContainer(Set<PacketHandlerContainer<T>> handlers) {
        int currentVersion = XReflection.MINOR_NUMBER;
        PacketHandlerContainer<T> supportedPacket = handlers.stream().filter(packetInfo -> packetInfo.isSupported(currentVersion)).findFirst().orElse(PacketHandlerContainer.unknown());

        String info = "Using %s version mapping '%s'. Current server version: %s"
                .formatted(
                        type.getSimpleName(),
                        supportedPacket.getHandler().protocolInfo(),
                        Bukkit.getMinecraftVersion()
                );

        QuickUtils.instance().getLogger().info(info);

        return supportedPacket;
    }

    private void checkSupportedVersion() {
        if (!packetContainer().isUnknownVersion()) return;
        throw new UnsupportedOperationException("%s of type '%s' currently does not support your version: %s.".formatted(PacketHandler.class.getSimpleName(), type.getName(), Bukkit.getMinecraftVersion()));
    }

    private void checkProtocolLib() {
        if (!requiresProtocolLib()) return;
        if (ProtocolLibHook.isEnabled()) return;
        throw new UnsupportedOperationException("%s of type '%s' currently only works with ProtocolLib.".formatted(PacketHandler.class.getSimpleName(), type.getName()));
    }

}
