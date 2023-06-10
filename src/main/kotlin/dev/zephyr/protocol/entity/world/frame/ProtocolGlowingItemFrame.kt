package dev.zephyr.protocol.entity.world.frame

import dev.zephyr.protocol.entity.type.EntityDirection
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolGlowingItemFrame(direction: EntityDirection, location: Location) : ProtocolItemFrame(location, direction, EntityType.GLOW_ITEM_FRAME)