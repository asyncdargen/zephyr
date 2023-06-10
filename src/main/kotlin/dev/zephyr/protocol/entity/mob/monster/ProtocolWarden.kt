package dev.zephyr.protocol.entity.mob.monster

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolWarden(location: Location) : ProtocolMonster(EntityType.WARDEN, location) {

    var angerLevel by metadata.item(16, MetadataType.Int, 0)

}