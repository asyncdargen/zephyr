package dev.zephyr.protocol.entity.mob.water

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolSquid(location: Location) : ProtocolWaterAnimal(EntityType.SQUID, location)