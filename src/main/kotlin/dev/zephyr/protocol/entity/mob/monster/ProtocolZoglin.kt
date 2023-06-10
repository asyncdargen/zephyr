package dev.zephyr.protocol.entity.mob.monster

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolZoglin(location: Location) : ProtocolMonster(EntityType.ZOGLIN, location) {

    var isBaby by metadata.item(16, MetadataType.Boolean, false)

}