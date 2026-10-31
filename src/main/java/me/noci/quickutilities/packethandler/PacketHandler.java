package me.noci.quickutilities.packethandler;

public interface PacketHandler<T extends PacketHandler<T>> {

    static <T extends PacketHandler<T>> T handler(Class<T> packetHandlerType) {
        return PacketHandlerFactory.getPacketHandler(packetHandlerType);
    }

    default String protocolInfo() {
        return "v." + major() + "." + minor() + "." + patch() + " (" + getClass().getSimpleName() + ")";
    }

    int major();

    int minor();

    default int patch() {
        return 0;
    }

}
