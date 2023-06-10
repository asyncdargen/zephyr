package dev.zephyr.protocol.entity.world.minecart

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolMinecartTNT(location: Location) : ProtocolAbstractMinecart(EntityType.MINECART_TNT, location)