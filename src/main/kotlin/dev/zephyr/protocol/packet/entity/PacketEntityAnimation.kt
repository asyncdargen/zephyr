package dev.zephyr.protocol.packet.entity

import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class PacketEntityAnimation : ProtocolPacket(PacketPlayOutType.ANIMATION) {

    var entityId by writer(0, integers)

    var animationId by writer(0, integers)

}