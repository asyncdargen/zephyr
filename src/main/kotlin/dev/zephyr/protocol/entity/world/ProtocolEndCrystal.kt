package dev.zephyr.protocol.entity.world

import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType
import java.util.*

@KotlinOpens
class ProtocolEndCrystal(location: Location) : ProtocolEntity(EntityType.ENDER_CRYSTAL, location) {

    var beamTarget by metadata.item(8, MetadataType.BlockPositionOptional, Optional.empty())
    var showBottom by metadata.item(9, MetadataType.Boolean, true)

}