package me.noci.quickutilities.quicktab.packets;


import me.noci.quickutilities.quicktab.builder.TabListTeam;
import me.noci.quickutilities.utils.ReflectionUtils;

public class VersionedTeamPacketUnknown implements VersionedTeamPacket {

    @Override
    public Object removeTeamPacket(TabListTeam team) {
        throw new UnsupportedOperationException(String.format("Your server version is currently not supported. Your are on version %s.", ReflectionUtils.getMajorVersion()));
    }

    @Override
    public Object createTeamPacket(TabListTeam team) {
        throw new UnsupportedOperationException(String.format("Your server version is currently not supported. Your are on version %s.", ReflectionUtils.getMajorVersion()));
    }

    @Override
    public String protocolVersion() {
        return "Unknown";
    }

}
