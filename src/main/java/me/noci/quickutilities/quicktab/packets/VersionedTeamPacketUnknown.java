package me.noci.quickutilities.quicktab.packets;


import com.cryptomorin.xseries.ReflectionUtils;
import me.noci.quickutilities.quicktab.builder.TabListTeam;

public class VersionedTeamPacketUnknown implements VersionedTeamPacket {

    @Override
    public Object removeTeamPacket(TabListTeam team) {
        throw new UnsupportedOperationException("Your server version is currently not supported. Your are on version %s.".formatted(ReflectionUtils.MINOR_NUMBER));
    }

    @Override
    public Object createTeamPacket(TabListTeam team) {
        throw new UnsupportedOperationException("Your server version is currently not supported. Your are on version %s.".formatted(ReflectionUtils.MINOR_NUMBER));
    }

    @Override
    public String protocolVersion() {
        return "Unknown";
    }

}
