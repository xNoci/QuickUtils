package me.noci.quickutilities.hooks.viaversion;

import lombok.Getter;

public class MinecraftVersion {

    private static final String SNAPSHOT_IDENTIFIER = "-SNAPSHOT";

    @Getter private final String name;
    @Getter private final int version;
    @Getter private final int patch;
    @Getter private final boolean snapshot;
    @Getter private final boolean unknown;

    protected MinecraftVersion(int version, int patch, boolean snapshot) {
        this.version = version;
        this.patch = patch;
        this.snapshot = snapshot;

        this.unknown = version == -1 | patch == -1;

        StringBuilder builder = new StringBuilder("1.");
        builder.append(version);
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
