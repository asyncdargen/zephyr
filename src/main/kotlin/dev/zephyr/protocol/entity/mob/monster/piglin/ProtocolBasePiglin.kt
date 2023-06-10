package dev.zephyr.protocol.entity.mob.monster.piglin

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.mob.monster.ProtocolMonster
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolBasePiglin(type: EntityType, location: Location) : ProtocolMonster(type, location) {

    var isImmuneToZombification by metadata.item(16, MetadataType.Boolean, false)

}