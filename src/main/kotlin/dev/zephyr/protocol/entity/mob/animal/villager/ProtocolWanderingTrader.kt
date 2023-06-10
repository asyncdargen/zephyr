package dev.zephyr.protocol.entity.mob.animal.villager

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolWanderingTrader(location: Location) : ProtocolAbstractVillager(EntityType.WANDERING_TRADER, location)