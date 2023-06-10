package dev.zephyr.protocol.entity.mob.animal

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.type.frog.FrogVariant
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType
import java.util.*

@KotlinOpens
class ProtocolFrog(location: Location) : ProtocolAnimal(EntityType.FROG, location) {

    var variant by metadata.item(17, MetadataType.FrogVariant, FrogVariant.TEMPERATE)
    var tongueTarget by metadata.item(18, MetadataType.IntOptional, OptionalInt.of(0))

}