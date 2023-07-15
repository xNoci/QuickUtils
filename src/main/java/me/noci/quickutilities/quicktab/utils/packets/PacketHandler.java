package me.noci.quickutilities.quicktab.utils.packets;

public interface PacketHandler<T extends PacketHandler<?>> {

    String protocolVersion();

    default String protocolInfo() {
        return protocolVersion() + " (" + getClass().getSimpleName() + ")";
    }

}
