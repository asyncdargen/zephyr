package dev.zephyr.protocol.entity.mob.monster.raider

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.mob.monster.ProtocolMonster
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolRaider(type: EntityType, location: Location) : ProtocolMonster(type, location) {

    var isCelebrating by metadata.item(16, MetadataType.Boolean, false)

}