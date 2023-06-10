package dev.zephyr.protocol.entity.mob.animal

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.type.rabbit.RabbitVariant
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolRabbit(location: Location) : ProtocolAnimal(EntityType.RABBIT, location) {

    var variant by metadata.item(17, MetadataType.Int, RabbitVariant.BROWN, RabbitVariant::ordinal) //Type

}