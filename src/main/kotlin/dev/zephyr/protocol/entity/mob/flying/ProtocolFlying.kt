package dev.zephyr.protocol.entity.mob.flying

import dev.zephyr.protocol.entity.mob.ProtocolMob
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolFlying(type: EntityType, location: Location) : ProtocolMob(type, location)