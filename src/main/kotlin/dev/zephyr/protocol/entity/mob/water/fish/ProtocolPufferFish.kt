package dev.zephyr.protocol.entity.mob.water.fish

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolPufferFish(location: Location) : ProtocolAbstractFish(EntityType.PUFFERFISH, location) {

    var puffState by metadata.item(17, MetadataType.Int, 0)

}