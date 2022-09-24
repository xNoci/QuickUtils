package me.noci.quickutilities.quicktab.utils;

import lombok.Getter;

public enum CollisionRule {

    /**
     * Always collide.
     */
    ALWAYS("always"),
    /**
     * Only collide with other teams.
     */
    FOR_OTHER_TEAMS("pushOtherTeams"),
    /**
     * Only collide with own team.
     */
    FOR_OWN_TEAM("pushOwnTeam"),
    /**
     * Never collide.
     */
    NEVER("never");

    @Getter private final String id;

    CollisionRule(String id) {
        this.id = id;
    }

}
