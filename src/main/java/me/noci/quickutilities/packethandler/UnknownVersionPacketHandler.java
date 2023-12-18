package me.noci.quickutilities.packethandler;

public class UnknownVersionPacketHandler<T extends PacketHandler<T>> implements PacketHandler<T> {

    @Override
    public String protocolVersion() {
        return "Unknown";
    }

    @Override
    public int[] supportedVersions() {
        return new int[]{-1};
    }

}
