package dev.zephyr.protocol.entity.mob

import dev.zephyr.protocol.entity.ProtocolLivingEntity
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolMob(type: EntityType, location: Location) : ProtocolLivingEntity(type, location) {

    var hasNoAI by metadata.bitMask(15, 0x01)
    var isLeftHanded by metadata.bitMask(15, 0x02)
    var isAggressive by metadata.bitMask(15, 0x04)

}