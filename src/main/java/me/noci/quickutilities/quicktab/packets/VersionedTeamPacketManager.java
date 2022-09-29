package me.noci.quickutilities.quicktab.packets;

import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import lombok.Getter;
import me.noci.quickutilities.QuickUtils;
import me.noci.quickutilities.quicktab.builder.TabListTeam;
import me.noci.quickutilities.utils.ProtocolLibHook;
import me.noci.quickutilities.utils.ReflectionUtils;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Set;

@ApiStatus.Internal
public class VersionedTeamPacketManager {

    private final static PacketInfo SUPPORTED_PACKET;

    static {
        PacketInfo unknownVersion = new PacketInfo(new VersionedTeamPacketUnknown(), -1);

        Set<PacketInfo> packetInfos = Sets.newHashSet();
        packetInfos.add(new PacketInfo(new VersionedTeamPacketV1_8(), 8));
        packetInfos.add(new PacketInfo(new VersionedTeamPacketV1_9(), 9, 10, 11, 12));
        packetInfos.add(new PacketInfo(new VersionedTeamPacketV1_13(), 13, 14, 15, 16));
        packetInfos.add(new PacketInfo(new VersionedTeamPacketV1_17(), 17, 18, 19));

        int currentVersion = ReflectionUtils.getMajorVersion();
        SUPPORTED_PACKET = packetInfos.stream().filter(packetInfo -> packetInfo.isVersionSupported(currentVersion)).findFirst().orElse(unknownVersion);

        String info = "Using TabListPacket Mapping %s. Current server version: %s (%s)"
                .formatted(
                        SUPPORTED_PACKET.handle.protocolInfo(),
                        ReflectionUtils.getVersion(),
                        ReflectionUtils.getMajorVersion()
                );

        QuickUtils.instance().getLogger().info(info);
    }

    public static Object getTeamRemovePacket(TabListTeam team) {
        checkProtocolLib();
        return SUPPORTED_PACKET.getHandle().removeTeamPacket(team);
    }

    public static Object getTeamCreatePacket(TabListTeam team) {
        checkProtocolLib();
        return SUPPORTED_PACKET.getHandle().createTeamPacket(team);
    }

    private static void checkProtocolLib() {
        if (!ProtocolLibHook.isEnabled()) {
            throw new UnsupportedOperationException("This currently only works with ProtocolLib.");
        }
    }

    private static class PacketInfo {

        @Getter private final VersionedTeamPacket handle;
        private final List<Integer> supportedVersions;

        private PacketInfo(VersionedTeamPacket handle, int... supportedVersions) {
            this.handle = handle;
            this.supportedVersions = Ints.asList(supportedVersions);
        }

        public boolean isVersionSupported(int version) {
            return this.supportedVersions.contains(version);
        }

    }

}
