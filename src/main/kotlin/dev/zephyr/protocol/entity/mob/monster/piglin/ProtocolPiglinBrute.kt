package dev.zephyr.protocol.entity.mob.monster.piglin

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolPiglinBrute(location: Location) : ProtocolBasePiglin(EntityType.PIGLIN_BRUTE, location)