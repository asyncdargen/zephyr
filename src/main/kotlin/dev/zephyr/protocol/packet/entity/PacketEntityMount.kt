package dev.zephyr.protocol.packet.entity

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class PacketEntityMount : ProtocolPacket(PacketPlayOutType.MOUNT) {

    var entityId by writer(0, integers)

    var entities by writer(0, integerArrays)

}