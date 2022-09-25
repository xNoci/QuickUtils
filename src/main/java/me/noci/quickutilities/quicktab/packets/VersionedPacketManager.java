package me.noci.quickutilities.quicktab.packets;

import me.noci.quickutilities.QuickUtils;
import me.noci.quickutilities.quicktab.builder.TabListTeam;
import me.noci.quickutilities.utils.ProtocolLibHook;
import me.noci.quickutilities.utils.ReflectionUtils;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;

@ApiStatus.Internal
public enum VersionedPacketManager {

    v1_8(new VersionedTeamPacketV1_8(), 8),
    v1_9(new VersionedTeamPacketV1_9(), 9, 10, 11, 12),
    v1_13(new VersionedTeamPacketV1_13(), 13, 14, 15, 16),
    v1_17(new VersionedTeamPacketV1_17(), 17, 18, 19),
    UNKNOWN_VERSION(new VersionedTeamPacketUnknown(), -1);

    private static final VersionedPacketManager VERSIONED_PACKET;

    static {
        VersionedPacketManager packet = UNKNOWN_VERSION;

        int currentServerVersion = ReflectionUtils.getMajorVersion();
        for (VersionedPacketManager value : values()) {
            if (Arrays.stream(value.supportedVersions).anyMatch(v -> v == currentServerVersion)) {
                packet = value;
                break;
            }
        }

        VERSIONED_PACKET = packet;

        String info = String.format("Using TabListPacket Mapping %s. Current server version: %s (%s)",
                VERSIONED_PACKET.handle.protocolInfo(),
                ReflectionUtils.getVersion(),
                ReflectionUtils.getMajorVersion());

        QuickUtils.instance().getLogger().info(info);
    }

    private final VersionedTeamPacket handle;
    private final int[] supportedVersions;

    VersionedPacketManager(VersionedTeamPacket handle, int... supportedVersions) {
        this.handle = handle;
        this.supportedVersions = supportedVersions;
    }

    public static Object removePacket(TabListTeam team) {
        checkProtocolLib();
        return VERSIONED_PACKET.handle.removeTeamPacket(team);
    }

    public static Object createPacket(TabListTeam team) {
        checkProtocolLib();
        return VERSIONED_PACKET.handle.createTeamPacket(team);
    }

    private static void checkProtocolLib() {
        if (!ProtocolLibHook.isEnabled()) {
            throw new UnsupportedOperationException("This currently only works with ProtocolLib.");
        }
    }

}
