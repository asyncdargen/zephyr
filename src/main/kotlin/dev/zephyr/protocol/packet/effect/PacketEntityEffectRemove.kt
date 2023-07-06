package dev.zephyr.protocol.packet.effect

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class PacketEntityEffectRemove : ProtocolPacket(PacketPlayOutType.REMOVE_ENTITY_EFFECT) {

    var entityId by writer(0, integers)
    var effectType by writer(0, effectTypes)

}