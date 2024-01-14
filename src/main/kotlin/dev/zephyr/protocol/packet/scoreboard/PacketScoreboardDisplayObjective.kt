package dev.zephyr.protocol.packet.scoreboard

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.scoreboard.DisplaySlot

@KotlinOpens
class PacketScoreboardDisplayObjective : ProtocolPacket(PacketPlayOutType.SCOREBOARD_DISPLAY_OBJECTIVE) {

    var objectiveName by writer(0, strings)
    var slot by writer(0, integers, DisplaySlot::positionId)

}

val DisplaySlot.positionId get() = when (this) {
    DisplaySlot.BELOW_NAME -> 2
    DisplaySlot.SIDEBAR -> 1
    DisplaySlot.PLAYER_LIST -> 0
    else -> ordinal
}