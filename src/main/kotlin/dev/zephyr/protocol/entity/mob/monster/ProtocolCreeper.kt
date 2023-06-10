package dev.zephyr.protocol.entity.mob.monster

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolCreeper(location: Location) : ProtocolMonster(EntityType.CREEPER, location) {

    var isFuse by metadata.item(16, MetadataType.Int, false) { if (it) 1 else -1 }
    var isCharged by metadata.item(17, MetadataType.Boolean, false)
    var isIgnited by metadata.item(18, MetadataType.Boolean, false)

}