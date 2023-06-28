package dev.zephyr.protocol.packet.entity

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class PacketEntityStatus : ProtocolPacket(PacketPlayOutType.ENTITY_STATUS) {

    var entityId by writer(0, integers)

    /**
    https://wiki.vg/Entity_statuses
    */
    var status by writer(0, bytes)

}