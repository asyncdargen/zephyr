package dev.zephyr.protocol.packet.entity

import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location

@KotlinOpens
class PacketEntityTeleport : ProtocolPacket(PacketPlayOutType.ENTITY_TELEPORT) {

    var entityId by writer(0, integers)

    var positionX by writer(0, doubles)
    var positionY by writer(1, doubles)
    var positionZ by writer(2, doubles)

    var rotationYaw by writer(0, bytes, AngleMapper)
    var rotationPitch by writer(1, bytes, AngleMapper)

    var isOnGround by writer(0, booleans)

    var location by writer<Location> {
        positionX = it.x
        positionY = it.y
        positionZ = it.z

        rotationYaw = it.yaw
        rotationPitch = it.pitch
    }

}