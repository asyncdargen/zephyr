package dev.zephyr.protocol.entity.mob.animal

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.DyeColor
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolSheep(location: Location) : ProtocolAnimal(EntityType.SHEEP, location) {

    var color by metadata.bitMask(17, DyeColor.WHITE, DyeColor::ordinal)
    var isSheared by metadata.bitMask(17, 0x10)

}