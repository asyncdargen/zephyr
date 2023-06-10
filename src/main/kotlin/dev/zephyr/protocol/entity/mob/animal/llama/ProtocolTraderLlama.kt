package dev.zephyr.protocol.entity.mob.animal.llama

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolTraderLlama(location: Location) : ProtocolLlama(location, EntityType.TRADER_LLAMA)