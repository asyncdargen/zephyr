package dev.zephyr.protocol.packet.entity

import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.util.Vector

@KotlinOpens
class PacketEntityVelocity : ProtocolPacket(PacketPlayOutType.ENTITY_VELOCITY) {

    var entityId by writer(0, integers)

    var motionX by writer(1, integers, VelocityMapper)
    var motionY by writer(2, integers, VelocityMapper)
    var motionZ by writer(3, integers, VelocityMapper)

    var velocity by writer<Vector> {
        motionX = it.x
        motionY = it.y
        motionZ = it.z
    }

}