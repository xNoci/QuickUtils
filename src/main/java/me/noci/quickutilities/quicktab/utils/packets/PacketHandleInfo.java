package me.noci.quickutilities.quicktab.utils.packets;

import com.google.common.primitives.Ints;
import lombok.Getter;

import java.util.List;

public class PacketHandleInfo<T extends PacketHandler<T>> {

    public static <T extends PacketHandler<T>> PacketHandleInfo<T> unknown(T handle) {
        return new PacketHandleInfo<>(handle, -1);
    }

    public static <T extends PacketHandler<T>> PacketHandleInfo<T> version(T handle, int... supportedVersions) {
        return new PacketHandleInfo<>(handle, supportedVersions);
    }

    @Getter private final T handle;
    private final List<Integer> supportedVersions;

    private PacketHandleInfo(T handle, int... supportedVersions) {
        this.handle = handle;
        this.supportedVersions = Ints.asList(supportedVersions);
    }

    public boolean isSupported(int version) {
        return this.supportedVersions.contains(version);
    }

}
