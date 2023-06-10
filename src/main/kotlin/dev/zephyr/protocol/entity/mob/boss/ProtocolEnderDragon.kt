package dev.zephyr.protocol.entity.mob.boss

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.mob.ProtocolMob
import dev.zephyr.protocol.entity.type.dragon.EnderDragonPhase
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolEnderDragon(location: Location) : ProtocolMob(EntityType.ENDER_DRAGON, location) {

    var dragonPhase by metadata.item(16, MetadataType.Int, EnderDragonPhase.HOVER, EnderDragonPhase::ordinal)

}