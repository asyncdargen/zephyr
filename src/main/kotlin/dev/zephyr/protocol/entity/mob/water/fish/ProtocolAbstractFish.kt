package dev.zephyr.protocol.entity.mob.water.fish

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.mob.water.ProtocolWaterAnimal
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolAbstractFish(type: EntityType, location: Location) : ProtocolWaterAnimal(type, location) {

    var fromBucket by metadata.item(16, MetadataType.Boolean, false)

}