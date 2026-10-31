package me.noci.quickutilities.hooks.viaversion;

import org.bukkit.entity.Player;

import java.util.stream.IntStream;

public enum Version {

    //https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol_version_numbers
    // Older versions are currently not supported
    V1_16(735), V1_16_1(736), V1_16_2(751, 738, 740, 741, 743, 744, 746, 748, 749, 750), V1_16_3(753, 752),
    V1_16_5(754, sPVN(1, 10)), //Same protocol version than 1.16.4, so it will be skipped
    V1_17(755, sPVN(11, 35)), V1_17_1(756, sPVN(36, 40)),
    V1_18_1(757, sPVN(41, 64)), //Same protocol version than 1.18, so it will be skipped
    V1_18_2(758, sPVN(65, 70)), V1_19(759, sPVN(74, 91)),
    V1_19_2(760, sPVN(92, 103)), //Same protocol version than 1.19.1, so it will be skipped
    V1_19_3(761, sPVN(104, 114)), V1_19_4(762, sPVN(115, 126)),
    V1_20_1(763, sPVN(127, 142)),//Same protocol version than 1.20, so it will be skipped
    V1_20_2(764, sPVN(144, 153)),
    V1_20_4(765, sPVN(154, 169)),//Same protocol version than 1.20.3, so it will be skipped
    V1_20_6(766, sPVN(170, 192)),//Same protocol version than 1.20.5, so it will be skipped
    V1_21_1(767, sPVN(193, 204)),//Same protocol version than 1.21, so it will be skipped
    V1_21_3(768, sPVN(205, 219)),//Same protocol version than 1.21.2, so it will be skipped
    V1_21_4(769, sPVN(220, 228)), V1_21_5(770, sPVN(229, 243)), V1_21_6(771, sPVN(244, 255)),
    V1_21_8(772, sPVN(256, 258)),//Same protocol version than 1.21.7, so it will be skipped
    V1_21_10(773, sPVN(259, 274)),//Same protocol version than 1.21.9, so it will be skipped
    V1_21_11(774, sPVN(275, 286)), V26_1_2(775, sPVN(287, 303)), V26_2(776, sPVN(306, 322)), V26_3(777, sPVN(323, 335)),
    UNKNOWN(-1), //If the protocol version is unknown or does not match any of the above
    UNREGISTERED_SNAPSHOT(-2); //Unregistered snapshot pvn

    private final int protocolVersion;
    private final int major;
    private final int minor;
    private final int patch;
    private final int[] snapshotProtocolVersions;

    Version(int protocolVersion) {
        this(protocolVersion, new int[] {});
    }

    Version(int protocolVersion, int... snapshotProtocolVersions) {
        this.protocolVersion = protocolVersion;
        this.snapshotProtocolVersions = snapshotProtocolVersions;

        if (name().startsWith("V")) {
            String[] components = name().substring(1).split("_");
            this.major = Integer.parseInt(components[0]);
            this.minor = components.length > 1 ? Integer.parseInt(components[1]) : 0;
            this.patch = components.length > 2 ? Integer.parseInt(components[2]) : 0;
        } else {
            this.major = -1;
            this.minor = -1;
            this.patch = -1;
        }
    }

    static MinecraftVersion getVersion(Player player) {
        int pvn = ViaVersionHook.getPlayerVersionProtocol(player.getUniqueId());

        if (pvn == -1) {
            return new MinecraftVersion(-1, -1, -1, false);
        }

        for (Version value : values()) {
            if (value.protocolVersion == pvn) {
                return new MinecraftVersion(value.major, value.minor, value.patch, false);
            }

            for (int sPVN : value.snapshotProtocolVersions) {
                if (sPVN == pvn) {
                    return new MinecraftVersion(value.major, value.minor, value.patch, true);
                }
            }
        }

        //Snapshot - 1.16.5 and versions above
        if (pvn >= 0x40000000) {
            return new MinecraftVersion(-1, -1, -1, true);
        }
        return new MinecraftVersion(-1, -1, -1, false);
    }

    private static int[] sPVN(int first, int last) {
        return IntStream.range(first, last + 1)
                .map(v -> 0x40000000 + v) //Convert snapshot to snapshot protocol version number
                .toArray();
    }

}