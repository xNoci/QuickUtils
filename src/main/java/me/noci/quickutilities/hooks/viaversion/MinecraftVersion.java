package me.noci.quickutilities.hooks.viaversion;

import lombok.Getter;

public class MinecraftVersion {

    private static final String SNAPSHOT_IDENTIFIER = "-SNAPSHOT";

    @Getter private final String name;
    @Getter private final int major;
    @Getter private final int minor;
    @Getter private final int patch;
    @Getter private final boolean snapshot;
    @Getter private final boolean unknown;

    protected MinecraftVersion(int major, int minor, int patch, boolean snapshot) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.snapshot = snapshot;

        this.unknown = major == -1 || minor == -1 | patch == -1;

        StringBuilder builder = new StringBuilder();
        builder.append(major);
        builder.append('.');
        builder.append(minor);
        if (this.patch != 0) {
            builder.append(".");
            builder.append(patch);
        }

        if (snapshot) {
            builder.append(SNAPSHOT_IDENTIFIER);
        }

        this.name = this.unknown ? "Unknown" : builder.toString();
    }

}
