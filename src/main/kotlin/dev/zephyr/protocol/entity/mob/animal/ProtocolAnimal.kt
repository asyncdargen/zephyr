package dev.zephyr.protocol.entity.mob.animal

import dev.zephyr.protocol.entity.mob.ProtocolAgeableMob
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolAnimal(type: EntityType, location: Location) : ProtocolAgeableMob(type, location)