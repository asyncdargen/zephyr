package dev.zephyr.protocol.packet.entity

import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class PacketEntityMetadata : ProtocolPacket(PacketPlayOutType.ENTITY_METADATA) {

    var entityId by writer(0, integers)

    var metadata by writer(0, dataValueCollectionModifier, MetadataMapper)
    var metadataItems by writer(0, dataValueCollectionModifier)

}