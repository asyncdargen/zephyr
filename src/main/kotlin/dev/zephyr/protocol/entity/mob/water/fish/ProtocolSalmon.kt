package dev.zephyr.protocol.entity.mob.water.fish

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolSalmon(location: Location) : ProtocolAbstractFish(EntityType.SALMON, location)