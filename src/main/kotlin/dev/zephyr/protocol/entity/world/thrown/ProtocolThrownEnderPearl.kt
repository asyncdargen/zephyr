package dev.zephyr.protocol.entity.world.thrown

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolThrownEnderPearl(location: Location, ownerEntityId: Int) :
    ProtocolThrownItemProjectile(EntityType.ENDER_PEARL, location, ownerEntityId)