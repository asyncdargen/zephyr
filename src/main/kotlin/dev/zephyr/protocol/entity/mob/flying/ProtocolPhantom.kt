package dev.zephyr.protocol.entity.mob.flying

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolPhantom(location: Location) : ProtocolFlying(EntityType.PHANTOM, location) {

    var size by metadata.item(16, MetadataType.Int, 0) //Size

}