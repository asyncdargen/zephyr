package dev.zephyr.protocol.entity.mob.water.fish

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.type.fish.TropicalFishVariant
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolTropicalFish(location: Location) : ProtocolAbstractFish(EntityType.TROPICAL_FISH, location) {

    var variant by metadata.item(17, MetadataType.Int, TropicalFishVariant.KOB, TropicalFishVariant::ordinal)

}