package dev.zephyr.protocol.entity.mob.animal

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolChicken(location: Location) : ProtocolAnimal(EntityType.CHICKEN, location)