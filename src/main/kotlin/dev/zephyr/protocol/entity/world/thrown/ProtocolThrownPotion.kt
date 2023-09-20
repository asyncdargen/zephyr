package dev.zephyr.protocol.entity.world.thrown

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolThrownPotion(location: Location, ownerEntityId: Int = 0) :
    ProtocolThrownItemProjectile(EntityType.SPLASH_POTION, location, ownerEntityId)