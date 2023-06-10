package dev.zephyr.protocol.entity.mob.animal

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.type.axolotl.AxolotlVariant
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolAxolotl(location: Location) : ProtocolAnimal(EntityType.AXOLOTL, location) {

    var variant by metadata.item(17, MetadataType.Int, AxolotlVariant.LUCY, AxolotlVariant::ordinal)
    var isPlayingDead by metadata.item(18, MetadataType.Boolean, false)
    var isSpawnedFromBucket by metadata.item(19, MetadataType.Boolean, false)

}