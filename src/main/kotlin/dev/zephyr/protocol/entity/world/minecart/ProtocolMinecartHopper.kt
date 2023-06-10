package dev.zephyr.protocol.entity.world.minecart

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolMinecartHopper(location: Location) : ProtocolAbstractMinecartContainer(EntityType.MINECART_HOPPER, location)