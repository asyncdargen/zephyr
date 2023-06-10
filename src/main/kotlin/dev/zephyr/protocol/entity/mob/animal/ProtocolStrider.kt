package dev.zephyr.protocol.entity.mob.animal

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolStrider(location: Location) : ProtocolAnimal(EntityType.STRIDER, location) {

    var boostTimeWithWarpedFungus by metadata.item(17, MetadataType.Int, 0)
    var isShaking by metadata.item(18, MetadataType.Boolean, false)
    var hasSaddle by metadata.item(19, MetadataType.Boolean, false)

}