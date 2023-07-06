package dev.zephyr.protocol.packet.effect

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.observable
import kotlin.experimental.or

@KotlinOpens
class PacketEntityEffect : ProtocolPacket(PacketPlayOutType.ENTITY_EFFECT) {

    var entityId by writer(0, integers)
    var effectType by writer(0, effectTypes)

    var level by writer(0, bytes)
    var duration by writer(1, integers)

    var flags by observable(0.toByte()) { _, flags -> bytes.write(1, flags) }

    var isAmbient by writer<Boolean> { flags = flags or 0x01 }
    var isShowParticles by writer<Boolean> { flags = flags or 0x02 }
    var isShowIcon by writer<Boolean> { flags = flags or 0x04 }

}