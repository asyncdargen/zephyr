package dev.zephyr.protocol.entity.mob.animal.horse

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolMule(location: Location) : ProtocolChestedHorse(EntityType.MULE, location)