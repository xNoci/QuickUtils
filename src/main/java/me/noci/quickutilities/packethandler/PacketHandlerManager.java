package me.noci.quickutilities.packethandler;

import com.cryptomorin.xseries.reflection.XReflection;
import lombok.Getter;
import me.noci.quickutilities.QuickUtils;
import me.noci.quickutilities.hooks.ProtocolLibHook;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PacketHandlerManager<T extends PacketHandler<T>> {

    @Getter private final Class<T> type;
    private final @Nullable T handle;
    private final boolean requiresProtocolLib;

    @SafeVarargs
    public PacketHandlerManager(boolean requiresProtocolLib, Class<T> type, T... packetHandlers) {
        this.requiresProtocolLib = requiresProtocolLib;
        this.type = type;


        List<T> handlers = Arrays.stream(packetHandlers)
                .sorted(
                        Comparator.<PacketHandler<T>>comparingInt(PacketHandler::version).thenComparing(PacketHandler::patch).reversed()
                )
                .collect(Collectors.toList());

        this.handle = findSupportedContainer(type, handlers);
    }

    private static <T extends PacketHandler<T>> T findSupportedContainer(Class<T> type, List<T> handlers) {
        T supportedHandle = handlers.stream().filter(handler -> XReflection.supports(handler.version(), handler.patch())).findFirst().orElse(null);

        String info = "Using %s version mapping '%s'. Current server version: %s"
                .formatted(
                        type.getSimpleName(),
                        Optional.ofNullable(supportedHandle).map(PacketHandler::protocolInfo).orElse("Unknown"),
                        Bukkit.getMinecraftVersion()
                );

        QuickUtils.instance().getLogger().info(info);

        return supportedHandle;
    }

    public T handle() {
        if (handle == null) {
            throw new UnsupportedOperationException("%s of type '%s' currently does not support your version: %s.".formatted(PacketHandler.class.getSimpleName(), type.getName(), Bukkit.getMinecraftVersion()));

        }
        checkProtocolLib();
        return handle;
    }

    private void checkProtocolLib() {
        if (!requiresProtocolLib) return;
        if (ProtocolLibHook.isEnabled()) return;
        throw new UnsupportedOperationException("%s of type '%s' currently only works with ProtocolLib.".formatted(PacketHandler.class.getSimpleName(), type.getName()));
    }

}
