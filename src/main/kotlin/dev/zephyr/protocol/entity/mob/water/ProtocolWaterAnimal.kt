package dev.zephyr.protocol.entity.mob.water

import dev.zephyr.protocol.entity.mob.ProtocolPathfinderMob
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolWaterAnimal(type: EntityType, location: Location) : ProtocolPathfinderMob(type, location)