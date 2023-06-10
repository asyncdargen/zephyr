package dev.zephyr.protocol.entity.mob.monster.raider

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolRavager(location: Location) : ProtocolRaider(EntityType.RAVAGER, location)