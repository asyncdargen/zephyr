package dev.zephyr.protocol.entity.mob.flying

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolGhast(location: Location) : ProtocolFlying(EntityType.GHAST, location) {

    var isAttacking by metadata.item(16, MetadataType.Boolean, false) //Is attacking

}