package dev.zephyr.protocol.scoreboard.type

enum class ScoreboardTeamTagVisibility(val handleName: String) {

    ALWAYS("always"),
    NEVER("never"),
    HIDE_FOR_OTHER_TEAMS("hideForOtherTeams"),
    HIDE_FOR_OWN_TEAM("hideForOwnTeam");


}