package me.noci.quickutilities.packethandler;

import com.google.common.primitives.Ints;
import lombok.Getter;

import java.util.List;

public class PacketHandlerInfo<T extends PacketHandler<T>> {

    public static <T extends PacketHandler<T>> PacketHandlerInfo<T> unknown(T handle) {
        return new PacketHandlerInfo<>(handle, -1);
    }

    public static <T extends PacketHandler<T>> PacketHandlerInfo<T> version(T handle, int... supportedVersions) {
        return new PacketHandlerInfo<>(handle, supportedVersions);
    }

    @Getter private final T handler;
    private final List<Integer> supportedVersions;

    private PacketHandlerInfo(T handler, int... supportedVersions) {
        this.handler = handler;
        this.supportedVersions = Ints.asList(supportedVersions);
    }

    public boolean isSupported(int version) {
        return this.supportedVersions.contains(version);
    }

}
