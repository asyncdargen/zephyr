package dev.zephyr.protocol.entity.mob.monster

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolBlaze(location: Location) : ProtocolMonster(EntityType.BLAZE, location) {

    var isOnFire by metadata.bitMask(16, 0x01)

}