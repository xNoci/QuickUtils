package me.noci.quickutilities.input.sign.packets;

import me.noci.quickutilities.packethandler.PacketHandlerManager;

public class SignPacketHandlerManager extends PacketHandlerManager<SignPacketHandler> {

    public SignPacketHandlerManager() {
        super(true, new SignPacketHandlerV1_8(), new SignPacketHandlerV1_9(), new SignPacketHandlerV1_18(), new SignPacketHandlerV1_20());
    }

}
