package dev.zephyr.protocol.packet.scoreboard

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.protocol.scoreboard.type.ScoreboardHealthDisplay
import dev.zephyr.protocol.scoreboard.type.ScoreboardObjectiveAction
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class PacketScoreboardObjective : ProtocolPacket(PacketPlayOutType.SCOREBOARD_OBJECTIVE) {

    var objectiveName by writer(0, strings)
    var action by writer(0, integers, ScoreboardObjectiveAction::ordinal)
    var healthDisplay by writer(0, getEnumModifier(ScoreboardHealthDisplay::class.java, 2))

    var displayName by writer(0, chatComponents, StringChatComponentMapper)
    var displayNameComponent by writer(0, chatComponents, ChatComponentMapper)

}