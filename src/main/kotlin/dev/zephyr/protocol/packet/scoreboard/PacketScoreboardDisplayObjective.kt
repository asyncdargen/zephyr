package dev.zephyr.protocol.packet.scoreboard

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.scoreboard.DisplaySlot

@KotlinOpens
class PacketScoreboardDisplayObjective : ProtocolPacket(PacketPlayOutType.SCOREBOARD_DISPLAY_OBJECTIVE) {

    var objectiveName by writer(0, strings)
    var slot by writer(0, integers, DisplaySlot::ordinal)

}