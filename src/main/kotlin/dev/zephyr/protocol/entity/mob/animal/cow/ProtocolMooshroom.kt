package dev.zephyr.protocol.entity.mob.animal.cow

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.type.cow.MooshroomCowType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolMooshroom(location: Location) : ProtocolCow(location, EntityType.MUSHROOM_COW) {

    var cowType by metadata.item(17, MetadataType.String, MooshroomCowType.RED, MooshroomCowType::type)

}