package dev.zephyr.protocol.entity.mob.monster.raider.illager

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolPillager(location: Location) : ProtocolAbstractIllager(EntityType.PILLAGER, location) {

    var isCharging by metadata.item(17, MetadataType.Boolean, false)

}