package me.noci.quickutilities.packethandler;

import com.google.common.primitives.Ints;
import lombok.Getter;

import java.util.List;

public class PacketHandlerInfo<T extends PacketHandler<T>> {

    @SuppressWarnings("unchecked")
    public static <T extends PacketHandler<T>> PacketHandlerInfo<T> unknown() {
        return new PacketHandlerInfo<>((T) new UnknownVersionPacketHandler<>(), true, -1);
    }

    public static <T extends PacketHandler<T>> PacketHandlerInfo<T> version(T handle, int... supportedVersions) {
        return new PacketHandlerInfo<>(handle, false, supportedVersions);
    }

    @Getter private final T handler;
    private final List<Integer> supportedVersions;
    @Getter private final boolean unknownVersion;


    private PacketHandlerInfo(T handler, boolean unknownVersion, int... supportedVersions) {
        this.handler = handler;
        this.supportedVersions = Ints.asList(supportedVersions);
        this.unknownVersion = unknownVersion;
    }

    public boolean isSupported(int version) {
        return this.supportedVersions.contains(version);
    }

}
