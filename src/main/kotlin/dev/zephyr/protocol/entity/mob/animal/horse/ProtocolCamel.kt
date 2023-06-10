package dev.zephyr.protocol.entity.mob.animal.horse

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolCamel(location: Location) : ProtocolAbstractHorse(EntityType.CAMEL, location) {

    var isDashing by metadata.item(18, MetadataType.Boolean, false)
    var lastPoseChangeTick by metadata.item(19, MetadataType.Long, 0)
}
