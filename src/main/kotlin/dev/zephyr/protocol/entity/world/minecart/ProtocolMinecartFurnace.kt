package dev.zephyr.protocol.entity.world.minecart

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolMinecartFurnace(location: Location) : ProtocolAbstractMinecart(EntityType.MINECART_FURNACE, location) {

    var hasFuel by metadata.item(14, MetadataType.Boolean, false)

}