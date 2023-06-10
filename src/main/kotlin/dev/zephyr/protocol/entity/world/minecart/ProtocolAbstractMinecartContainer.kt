package dev.zephyr.protocol.entity.world.minecart

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolAbstractMinecartContainer(type: EntityType, location: Location) : ProtocolAbstractMinecart(type, location)