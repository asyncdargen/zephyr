package dev.zephyr.protocol.entity.world.boat

import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.type.boat.BoatVariant
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolBoat(location: Location, type: EntityType = EntityType.BOAT) : ProtocolEntity(type, location) {

    var timeSinceLastHit by metadata.item(8, MetadataType.Int, 0)
    var forwardDirection by metadata.item(9, MetadataType.Int, 1)
    var damageTaken by metadata.item(10, MetadataType.Float, 0f)
    var variant by metadata.item(11, MetadataType.Int, BoatVariant.OAK, BoatVariant::ordinal)
    var isLeftPaddleTurning by metadata.item(12, MetadataType.Boolean, false)
    var isRightPaddleTurning by metadata.item(13, MetadataType.Boolean, false)
    var splashTimer by metadata.item(14, MetadataType.Int, 0)

}