package dev.zephyr.protocol.entity.mob.monster.guadian

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolElderGuardian(location: Location) : ProtocolGuardian(location, EntityType.ELDER_GUARDIAN)