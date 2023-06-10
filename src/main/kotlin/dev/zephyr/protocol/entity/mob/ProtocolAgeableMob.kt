package dev.zephyr.protocol.entity.mob

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolAgeableMob(type: EntityType, location: Location) : ProtocolPathfinderMob(type, location) {

    var isBaby by metadata.item(16, MetadataType.Boolean, false)

}