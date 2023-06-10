package dev.zephyr.protocol.entity.world.minecart

import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolAbstractMinecart(type: EntityType, location: Location) : ProtocolEntity(type, location) {

    var shakingPower by metadata.item(8, MetadataType.Int, 0)
    var shakingDirection by metadata.item(9, MetadataType.Int, 1)
    var shakingMultiplier by metadata.item(10, MetadataType.Float, 0f)
    var customBlockIDAndDamage by metadata.item(11, MetadataType.Int, 0)
    var customBlockYPosition by metadata.item(12, MetadataType.Int, 6)
    var showCustomBlock by metadata.item(13, MetadataType.Boolean, false)

}