package dev.zephyr.protocol.entity.world.boat

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolChestBoat(location: Location) : ProtocolBoat(location, EntityType.CHEST_BOAT)