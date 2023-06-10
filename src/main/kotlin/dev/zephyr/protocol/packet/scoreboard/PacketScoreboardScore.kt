package dev.zephyr.protocol.packet.scoreboard

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class PacketScoreboardScore : ProtocolPacket(PacketPlayOutType.SCOREBOARD_SCORE) {

    var scoreName by writer(0, strings)
    var objectiveName by writer(1, strings)
    var action by writer(0, scoreboardActions)
    var score by writer(0, integers)

}