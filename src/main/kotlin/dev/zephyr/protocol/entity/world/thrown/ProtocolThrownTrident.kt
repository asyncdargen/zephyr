package dev.zephyr.protocol.entity.world.thrown

import dev.zephyr.protocol.entity.world.arrow.ProtocolAbstractArrow
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolThrownTrident(location: Location) : ProtocolAbstractArrow(EntityType.TRIDENT, location) {

    var loyaltyLevel by metadata.item(10, MetadataType.Int, 0)
    var hasEnchantmentGlint by metadata.item(11, MetadataType.Boolean, false)

}