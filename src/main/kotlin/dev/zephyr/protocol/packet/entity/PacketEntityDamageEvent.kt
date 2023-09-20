package dev.zephyr.protocol.packet.entity

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.entity.type.EntityDamageSource
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class PacketEntityDamageEvent : ProtocolPacket(PacketPlayOutType.DAMAGE_EVENT) {

    var entityId by writer(0, integers)
    var sourceId by writer(1, integers)
    var sourceType: EntityDamageSource by writer { sourceId = it.ordinal }

    var damagerId by writer(2, integers)
    var damagerDirectId by writer(2, integers)

}