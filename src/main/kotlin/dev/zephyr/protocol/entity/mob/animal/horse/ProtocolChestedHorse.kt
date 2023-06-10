package dev.zephyr.protocol.entity.mob.animal.horse

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolChestedHorse(type: EntityType, location: Location) : ProtocolAbstractHorse(type, location) {

    var hasChest by metadata.item(18, MetadataType.Boolean, false)

}