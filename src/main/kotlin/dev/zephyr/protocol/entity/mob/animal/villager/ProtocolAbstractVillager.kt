package dev.zephyr.protocol.entity.mob.animal.villager

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.mob.ProtocolAgeableMob
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolAbstractVillager(type: EntityType, location: Location) : ProtocolAgeableMob(type, location) {

    var headShakeTimer by metadata.item(17, MetadataType.Int, 0) //Head shake timer (starts at 40, decrements each tick)

}