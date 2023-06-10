package dev.zephyr.protocol.entity.world

import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolFishingHook(location: Location, val ownerEntityId: Int) :
    ProtocolEntity(EntityType.FISHING_HOOK, ownerEntityId, location) {

    var hookedEntityId by metadata.item(8, MetadataType.Int, 0) { it + 1 }
    var isCatchable by metadata.item(9, MetadataType.Boolean, false)

}