package dev.zephyr.protocol.entity.mob.golem

import dev.zephyr.protocol.entity.mob.ProtocolPathfinderMob
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolAbstractGolem(type: EntityType, location: Location) : ProtocolPathfinderMob(type, location)