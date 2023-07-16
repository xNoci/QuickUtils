package me.noci.quickutilities.packethandler;

public interface PacketHandler<T extends PacketHandler<?>> {

    String protocolVersion();

    default String protocolInfo() {
        return protocolVersion() + " (" + getClass().getSimpleName() + ")";
    }

}
