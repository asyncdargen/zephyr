package dev.zephyr.protocol.entity.mob.monster

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolGiant(location: Location) : ProtocolMonster(EntityType.GIANT, location)