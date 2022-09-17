package me.noci.quickutilities.quicktab.packets;


import me.noci.quickutilities.quicktab.TabListTeam;
import me.noci.quickutilities.utils.ReflectionUtils;

public class TabListTeamPacketUnknown implements TabListTeamPacket {

    @Override
    public Object removeTeamPacket(TabListTeam team) {
        throw new UnsupportedOperationException(String.format("Your server version is currently not supported. Your are on version %s.", ReflectionUtils.getMajorVersion()));
    }

    @Override
    public Object createTeamPacket(TabListTeam team) {
        throw new UnsupportedOperationException(String.format("Your server version is currently not supported. Your are on version %s.", ReflectionUtils.getMajorVersion()));
    }

}
