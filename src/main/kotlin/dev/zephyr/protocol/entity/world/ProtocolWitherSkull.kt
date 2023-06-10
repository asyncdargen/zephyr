package dev.zephyr.protocol.entity.world

import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolWitherSkull(location: Location) : ProtocolEntity(EntityType.WITHER_SKULL, location) {

    var isInvulnerable by metadata.item(8, MetadataType.Boolean, false)

}