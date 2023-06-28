package dev.zephyr.protocol.packet.block

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.asProtocolBlockPosition
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import kotlin.math.floor

@KotlinOpens
class PacketBlockDestroyAnimation : ProtocolPacket(PacketPlayOutType.BLOCK_BREAK_ANIMATION) {

    var entityId by writer(0, integers)
    var position by writer(0, blockPositionModifier)

    var location by writer(0, blockPositionModifier, Location::asProtocolBlockPosition)

    var stage by writer(1, integers)
    var stageDouble by writer<Double> { stage = (floor(it).toInt() - 1).coerceAtMost(9) }
    var percent by writer<Double> { stageDouble = it / 100 }

}
