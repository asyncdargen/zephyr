package dev.zephyr.protocol.entity.mob.animal

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolHoglin(location: Location) : ProtocolAnimal(EntityType.HOGLIN, location) {

    var isImmuneToZombification by metadata.item(17, MetadataType.Boolean, false)

}