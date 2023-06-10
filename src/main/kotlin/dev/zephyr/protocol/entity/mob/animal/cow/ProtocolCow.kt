package dev.zephyr.protocol.entity.mob.animal.cow

import dev.zephyr.protocol.entity.mob.animal.ProtocolAnimal
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolCow(location: Location, type: EntityType = EntityType.COW) : ProtocolAnimal(type, location)