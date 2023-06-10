package dev.zephyr.protocol.entity.mob.monster.raider.illager

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolIllusioner(location: Location) : ProtocolSpellcasterIllager(EntityType.ILLUSIONER, location)