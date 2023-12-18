package me.noci.quickutilities.packethandler;

import com.google.common.primitives.Ints;
import lombok.Getter;

import java.util.List;

public class PacketHandlerContainer<T extends PacketHandler<T>> {

    @SuppressWarnings("unchecked")
    public static <T extends PacketHandler<T>> PacketHandlerContainer<T> unknown() {
        return new PacketHandlerContainer<>((T) new UnknownVersionPacketHandler<>(), true, -1);
    }

    @SuppressWarnings("unchecked")
    public static <T extends PacketHandler<T>> PacketHandlerContainer<T> version(PacketHandler<T> handle, int[] supportedVersions) {
        return new PacketHandlerContainer<>((T) handle, false, supportedVersions);
    }

    @Getter private final T handler;
    private final List<Integer> supportedVersions;
    @Getter private final boolean unknownVersion;


    private PacketHandlerContainer(T handler, boolean unknownVersion, int... supportedVersions) {
        this.handler = handler;
        this.supportedVersions = Ints.asList(supportedVersions);
        this.unknownVersion = unknownVersion;
    }

    public boolean isSupported(int version) {
        return this.supportedVersions.contains(version);
    }

}
