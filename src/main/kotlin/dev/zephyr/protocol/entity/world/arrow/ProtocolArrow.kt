package dev.zephyr.protocol.entity.world.arrow

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolArrow(location: Location) : ProtocolAbstractArrow(EntityType.ARROW, location) {

    var color by metadata.item(10, MetadataType.Int, -1)

}