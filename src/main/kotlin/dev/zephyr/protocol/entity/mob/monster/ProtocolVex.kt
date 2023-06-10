package dev.zephyr.protocol.entity.mob.monster

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolVex(location: Location) : ProtocolMonster(EntityType.VEX, location) {

    var isAttacking by metadata.bitMask(16, 0x01)

}