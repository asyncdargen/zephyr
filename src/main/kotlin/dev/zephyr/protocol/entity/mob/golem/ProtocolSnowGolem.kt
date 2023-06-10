package dev.zephyr.protocol.entity.mob.golem

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolSnowGolem(location: Location) : ProtocolAbstractGolem(EntityType.SNOWMAN, location) {

    var hasNoPumpkinHat by metadata.bitMask(16, 0x00)
    var hasPumpkinHat by metadata.bitMask(16, 0x10)

}