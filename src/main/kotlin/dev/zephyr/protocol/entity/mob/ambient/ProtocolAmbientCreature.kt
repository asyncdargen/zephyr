package dev.zephyr.protocol.entity.mob.ambient

import dev.zephyr.protocol.entity.mob.ProtocolMob
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolAmbientCreature(type: EntityType, location: Location) : ProtocolMob(type, location)