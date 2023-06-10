package dev.zephyr.protocol.packet.entity

import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class PacketEntityLook : ProtocolPacket(PacketPlayOutType.ENTITY_LOOK) {

    var entityId by writer(0, integers)

    var rotationYaw by writer(0, bytes, AngleMapper)
    var rotationPitch by writer(1, bytes, AngleMapper)

}