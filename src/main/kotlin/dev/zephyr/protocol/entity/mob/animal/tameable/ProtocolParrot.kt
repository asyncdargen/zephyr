package dev.zephyr.protocol.entity.mob.animal.tameable

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.type.parrot.ParrotVariant
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolParrot(location: Location) : ProtocolTameableAnimal(EntityType.PARROT, location) {

    var variant by metadata.item(19, MetadataType.Int, ParrotVariant.RED, ParrotVariant::ordinal)

}