package me.noci.quickutilities.quicktab.packets;

import me.noci.quickutilities.quicktab.TabListTeam;
import me.noci.quickutilities.utils.ProtocolLibHook;
import me.noci.quickutilities.utils.ReflectionUtils;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;

@ApiStatus.Internal
public enum TabListPacketManager {

    v1_8(new TabListTeamPacketV1_8(), 8),
    UNKNOWN_VERSION(new TabListTeamPacketUnknown(), -1);

    private final TabListTeamPacket versionedTeamPacket;
    private final int[] supportedVersions;

    TabListPacketManager(TabListTeamPacket versionedTeamPacket, int... supportedVersions) {
        this.versionedTeamPacket = versionedTeamPacket;
        this.supportedVersions = supportedVersions;
    }

    public static Object removePacket(TabListTeam team) {
        checkProtocolLib();
        return getVersionedTeamPacket().removeTeamPacket(team);
    }

    public static Object createPacket(TabListTeam team) {
        checkProtocolLib();
        return getVersionedTeamPacket().createTeamPacket(team);
    }

    private static TabListTeamPacket getVersionedTeamPacket() {
        int currentServerVersion = ReflectionUtils.getMajorVersion();

        for (TabListPacketManager value : values()) {
            if (Arrays.stream(value.supportedVersions).anyMatch(v -> v == currentServerVersion))
                return value.versionedTeamPacket;
        }

        return UNKNOWN_VERSION.versionedTeamPacket;
    }

    private static void checkProtocolLib() {
        if (!ProtocolLibHook.isEnabled()) {
            throw new UnsupportedOperationException("This currently only works with ProtocolLib.");
        }
    }

}
