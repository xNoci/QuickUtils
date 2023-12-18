package me.noci.quickutilities.packethandler;

public interface PacketHandler<T extends PacketHandler<T>> {

    String protocolVersion();

    default String protocolInfo() {
        return protocolVersion() + " (" + getClass().getSimpleName() + ")";
    }

    int[] supportedVersions();

    default PacketHandlerContainer<T> createContainer() {
        return PacketHandlerContainer.version(this, supportedVersions());
    }

}
