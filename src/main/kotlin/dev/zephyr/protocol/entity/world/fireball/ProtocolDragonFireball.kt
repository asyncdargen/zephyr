package dev.zephyr.protocol.entity.world.fireball

import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolDragonFireball(location: Location) : ProtocolEntity(EntityType.DRAGON_FIREBALL, location)