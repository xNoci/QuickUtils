package me.noci.quickutilities.packethandler;

public class UnknownVersionPacketHandler<T extends PacketHandler<T>> implements PacketHandler<T> {

    @Override
    public String protocolVersion() {
        return "Unknown";
    }

}
