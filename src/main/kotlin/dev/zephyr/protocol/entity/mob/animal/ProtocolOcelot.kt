package dev.zephyr.protocol.entity.mob.animal

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolOcelot(location: Location) : ProtocolAnimal(EntityType.OCELOT, location) {

    var isTrusting by metadata.item(17, MetadataType.Boolean, false)

}