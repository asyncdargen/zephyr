package dev.zephyr.protocol.entity.mob.golem

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolIronGolem(location: Location) : ProtocolAbstractGolem(EntityType.IRON_GOLEM, location) {

    var isPlayerCreated by metadata.bitMask(16, 0x01)

}