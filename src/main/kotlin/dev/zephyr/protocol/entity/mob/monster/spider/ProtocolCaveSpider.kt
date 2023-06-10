package dev.zephyr.protocol.entity.mob.monster.spider

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolCaveSpider(location: Location) : ProtocolSpider(location, EntityType.CAVE_SPIDER)