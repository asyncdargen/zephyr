package dev.zephyr.protocol.packet.world

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.protocol.world.Position
import dev.zephyr.protocol.world.blockPosition
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import kotlin.math.floor

@KotlinOpens
class PacketBlockDestroyAnimation : ProtocolPacket(PacketPlayOutType.BLOCK_BREAK_ANIMATION) {

    var entityId by writer(0, integers)
    var blockPosition by writer(0, blockPositionModifier)

    var location by writer(0, blockPositionModifier, Location::blockPosition)
    var position by writer(0, blockPositionModifier, Position::blockPos)

    var stage by writer(1, integers)
    var stageDouble by writer<Double> { stage = (floor(it).toInt() - 1).coerceAtMost(9) }
    var percent by writer<Double> { stageDouble = it / 10.0 }

}
