package dev.zephyr.protocol.entity.world

import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolEvokerFangs(location: Location) : ProtocolEntity(EntityType.EVOKER_FANGS, location)