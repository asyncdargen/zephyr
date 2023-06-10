package dev.zephyr.protocol.entity.world

import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolPrimedTnt(location: Location) : ProtocolEntity(EntityType.PRIMED_TNT, location) {

    var fuseTime by metadata.item(8, MetadataType.Int, 80)

}