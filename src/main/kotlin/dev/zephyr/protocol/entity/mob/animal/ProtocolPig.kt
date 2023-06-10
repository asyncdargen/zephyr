package dev.zephyr.protocol.entity.mob.animal

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolPig(location: Location) : ProtocolAnimal(EntityType.PIG, location) {

    var hasSaddle by metadata.item(17, MetadataType.Boolean, false)
    var boostTimeWithCarrot by metadata.item(18, MetadataType.Int, 0)

}