package dev.zephyr.protocol.entity.mob.monster.skeleton

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolSkeleton(location: Location) : ProtocolAbstractSkeleton(EntityType.SKELETON, location)