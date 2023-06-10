package dev.zephyr.protocol.entity.mob.monster.skeleton

import dev.zephyr.protocol.entity.mob.monster.ProtocolMonster
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolAbstractSkeleton(type: EntityType, location: Location) : ProtocolMonster(type, location)