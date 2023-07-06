package dev.zephyr.protocol.entity.mob.monster.zombie

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.mob.monster.ProtocolMonster
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolZombie(location: Location, type: EntityType = EntityType.ZOMBIE) : ProtocolMonster(type, location) {

    var isBaby by metadata.item(16, MetadataType.Boolean, false)
    var isBecomingDrowned by metadata.item(18, MetadataType.Boolean, false)

}