package dev.zephyr.protocol.entity.mob.animal.horse

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolDonkey(location: Location) : ProtocolChestedHorse(EntityType.DONKEY, location)