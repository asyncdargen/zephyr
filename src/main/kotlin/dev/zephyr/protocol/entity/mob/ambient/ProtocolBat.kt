package dev.zephyr.protocol.entity.mob.ambient

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolBat(location: Location) : ProtocolAmbientCreature(EntityType.BAT, location) {

    var isHanging by metadata.bitMask(16, 0x01)

}