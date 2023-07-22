package dev.zephyr.protocol.entity.mob

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolAllay(location: Location) : ProtocolPathfinderMob(EntityType.ALLAY, location) {
}