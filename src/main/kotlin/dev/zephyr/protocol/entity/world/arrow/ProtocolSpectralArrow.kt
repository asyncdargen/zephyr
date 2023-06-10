package dev.zephyr.protocol.entity.world.arrow

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolSpectralArrow(location: Location) : ProtocolAbstractArrow(EntityType.SPECTRAL_ARROW, location)