package dev.zephyr.protocol.entity.mob.monster.raider.illager

import dev.zephyr.protocol.entity.mob.monster.raider.ProtocolRaider
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolAbstractIllager(type: EntityType, location: Location) : ProtocolRaider(type, location)