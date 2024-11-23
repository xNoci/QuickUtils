package me.noci.quickutilities.packethandler;

public interface PacketHandler<T extends PacketHandler<T>> {

    static <T extends PacketHandler<T>> T handler(Class<T> packetHandlerType) {
        return PacketHandlerFactory.getPacketHandler(packetHandlerType);
    }

    default String protocolInfo() {
        return "v1." + version() + "." + patch() + " (" + getClass().getSimpleName() + ")";
    }

    int version();

    default int patch() {
        return 0;
    }

}
