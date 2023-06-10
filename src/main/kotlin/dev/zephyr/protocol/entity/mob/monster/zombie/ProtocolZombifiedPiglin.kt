package dev.zephyr.protocol.entity.mob.monster.zombie

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolZombifiedPiglin(location: Location) : ProtocolZombie(EntityType.ZOMBIFIED_PIGLIN, location)