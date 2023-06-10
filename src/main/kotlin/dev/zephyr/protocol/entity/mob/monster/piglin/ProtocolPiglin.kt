package dev.zephyr.protocol.entity.mob.monster.piglin

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolPiglin(location: Location) : ProtocolBasePiglin(EntityType.PIGLIN, location) {

    var isBaby by metadata.item(17, MetadataType.Boolean, false)
    var isChargingCrossbow by metadata.item(18, MetadataType.Boolean, false)
    var isDancing by metadata.item(19, MetadataType.Boolean, false)

}