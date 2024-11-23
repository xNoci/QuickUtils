package me.noci.quickutilities.packethandler;

public interface PacketHandler<T extends PacketHandler<T>> {

    static <T extends PacketHandler<T>> T handler(Class<T> packetHandlerType) {
        return PacketHandlerFactory.getPacketHandler(packetHandlerType);
    }

    String protocolVersion();

    default String protocolInfo() {
        return protocolVersion() + " (" + getClass().getSimpleName() + ")";
    }

    int[] supportedVersions();

    default PacketHandlerContainer<T> createContainer() {
        return PacketHandlerContainer.version(this, supportedVersions());
    }

}
