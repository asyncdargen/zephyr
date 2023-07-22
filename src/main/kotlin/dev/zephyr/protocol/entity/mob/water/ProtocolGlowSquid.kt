package dev.zephyr.protocol.entity.mob.water

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolGlowSquid(location: Location) : ProtocolSquid(location, EntityType.GLOW_SQUID) {

    var darkTicksRemaining by metadata.item(16, MetadataType.Int, 0)

}