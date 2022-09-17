package me.noci.quickutilities.quicktab.utils;

import lombok.Getter;

public enum NameTagVisibility {

    /**
     * Always show the player's name tag.
     */
    ALWAYS("always"),
    /**
     * Show the player's name tag only to his own team members.
     */
    HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
    /**
     * Show the player's name tag only to members of other teams.
     */
    HIDE_FOR_OWN_TEAM("hideForOwnTeam"),
    /**
     * Never show the player's name tag.
     */
    NEVER("never");

    @Getter private final String id;

    NameTagVisibility(String id) {
        this.id = id;
    }

}
