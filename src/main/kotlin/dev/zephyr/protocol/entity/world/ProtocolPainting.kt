package dev.zephyr.protocol.entity.world

import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.type.EntityDirection
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolPainting(direction: EntityDirection, location: Location) :
    ProtocolEntity(EntityType.PAINTING, direction.ordinal, location) {

    //todo: meta type
//    var paintingType by metadata.item(8, MetadataType.ArtVariant, ArtVariant.KEBAB)

}