package dev.zephyr.protocol.scoreboard.type

enum class ScoreboardTeamCollision(val handleName: String) {

    ALWAYS("always"),
    NEVER("never"),
    HIDE_FOR_OTHER_TEAMS("pushOtherTeams"),
    HIDE_FOR_OWN_TEAM("pushOwnTeam")

}