package dev.zephyr.protocol.entity.mob.animal.tameable

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.DyeColor
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolWolf(location: Location) : ProtocolTameableAnimal(EntityType.WOLF, location) {

    var isBegging by metadata.item(19, MetadataType.Boolean, false)
    var collarColor by metadata.item(20, MetadataType.Int, DyeColor.RED, DyeColor::ordinal)
    var angerTime by metadata.item(21, MetadataType.Int, 0)

}