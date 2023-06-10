package dev.zephyr.protocol.packet.entity

import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class PacketEntityHeadRotation : ProtocolPacket(PacketPlayOutType.ENTITY_HEAD_ROTATION) {

    var entityId by writer(0, integers)

    var rotationHeadYaw by writer(0, bytes, AngleMapper)

}