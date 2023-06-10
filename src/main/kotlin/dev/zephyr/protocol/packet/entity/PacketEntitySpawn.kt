package dev.zephyr.protocol.packet.entity

import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.util.Vector

@KotlinOpens
class PacketEntitySpawn : ProtocolPacket(PacketPlayOutType.SPAWN_ENTITY) {

    var entityId by writer(0, integers)
    var entityUUID by writer(0, uuiDs)

    var entityType by writer(0, entityTypeModifier)
    var entityData by writer(4, integers)

    var positionX by writer(0, doubles)
    var positionY by writer(1, doubles)
    var positionZ by writer(2, doubles)

    var rotationYaw by writer(0, bytes, AngleMapper)
    var rotationPitch by writer(1, bytes, AngleMapper)
    var rotationHeadYaw by writer(2, bytes, AngleMapper)

    var motionX by writer(1, integers, VelocityMapper)
    var motionY by writer(2, integers, VelocityMapper)
    var motionZ by writer(3, integers, VelocityMapper)

    var location by writer<Location> {
        positionX = it.x
        positionY = it.y
        positionZ = it.z

        rotationYaw = it.yaw
        rotationHeadYaw = it.yaw
        rotationPitch = it.pitch
    }

    var velocity by writer<Vector> {
        motionX = it.x
        motionY = it.y
        motionZ = it.z
    }

}