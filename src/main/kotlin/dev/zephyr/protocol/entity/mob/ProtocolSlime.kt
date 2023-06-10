package dev.zephyr.protocol.entity.mob

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolSlime(location: Location) : ProtocolMob(EntityType.SLIME, location) {

    var size by metadata.item(16, MetadataType.Int, 1)

}