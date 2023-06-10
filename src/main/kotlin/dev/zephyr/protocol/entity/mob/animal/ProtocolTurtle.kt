package dev.zephyr.protocol.entity.mob.animal

import com.comphenix.protocol.wrappers.BlockPosition
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolTurtle(location: Location) : ProtocolAnimal(EntityType.TURTLE, location) {

    var homePosition by metadata.item(17, MetadataType.BlockPosition, BlockPosition.ORIGIN)
    var hasEgg by metadata.item(18, MetadataType.Boolean, false)
    var isLayingEgg by metadata.item(19, MetadataType.Boolean, false)
    var travelPosition by metadata.item(20, MetadataType.BlockPosition, BlockPosition.ORIGIN)
    var isGoingHome by metadata.item(21, MetadataType.Boolean, false)
    var isTraveling by metadata.item(22, MetadataType.Boolean, false)

}