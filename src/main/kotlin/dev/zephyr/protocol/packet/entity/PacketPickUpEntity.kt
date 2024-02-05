package dev.zephyr.protocol.packet.entity

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class PacketPickUpEntity : ProtocolPacket(PacketPlayOutType.COLLECT)  {

    var entityId by writer(0, integers)
    var collectorEntityId by writer(1, integers)
    var amount by writer(2,integers)

}