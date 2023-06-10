package dev.zephyr.protocol.packet.entity

import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class PacketEntityDestroy : ProtocolPacket(PacketPlayOutType.ENTITY_DESTROY) {

    var entitiesIds by writer(0, modifier, IntListMapper)

}