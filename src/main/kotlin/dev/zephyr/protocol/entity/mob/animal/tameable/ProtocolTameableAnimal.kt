package dev.zephyr.protocol.entity.mob.animal.tameable

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.mob.animal.ProtocolAnimal
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType
import java.util.*

@KotlinOpens
class ProtocolTameableAnimal(type: EntityType, location: Location) : ProtocolAnimal(type, location) {

    var isSitting by metadata.bitMask(17, 0x01)
    var isTamed by metadata.bitMask(17, 0x04)
    var owner by metadata.item(18, MetadataType.UUIDOptional, Optional.empty())

}