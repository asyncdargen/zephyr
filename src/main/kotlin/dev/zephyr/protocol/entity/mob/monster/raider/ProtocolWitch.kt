package dev.zephyr.protocol.entity.mob.monster.raider

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolWitch(location: Location) : ProtocolRaider(EntityType.WITCH, location) {

    var isDrinkingPotion by metadata.item(17, MetadataType.Boolean, false)

}