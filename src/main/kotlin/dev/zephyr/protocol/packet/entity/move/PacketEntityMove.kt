package dev.zephyr.protocol.packet.entity.move

import com.comphenix.protocol.PacketType
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.util.Vector

@KotlinOpens
class PacketEntityMove(type: PacketType = PacketPlayOutType.REL_ENTITY_MOVE) : ProtocolPacket(type) {

    var entityId by writer(0, integers)

    var deltaX by writer(0, shorts, MoveMapper)
    var deltaY by writer(1, shorts, MoveMapper)
    var deltaZ by writer(2, shorts, MoveMapper)

    var motion by writer<Vector> {
        deltaX = it.x
        deltaY = it.y
        deltaZ = it.z
    }

    var isOnGround by writer(0, booleans)

}