package dev.zephyr.protocol.entity.mob.monster.zombie

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolHusk(location: Location) : ProtocolZombie(location, EntityType.HUSK)