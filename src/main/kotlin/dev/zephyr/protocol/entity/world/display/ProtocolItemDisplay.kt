package dev.zephyr.protocol.entity.world.display

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.type.display.ItemDisplayTransform
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

@KotlinOpens
class ProtocolItemDisplay(location: Location) : ProtocolDisplay(location, EntityType.ITEM_DISPLAY) {

    var item by metadata.item(22, MetadataType.ItemStack, ItemStack(Material.AIR))
    var transform by metadata.item(23, MetadataType.Byte, ItemDisplayTransform.NONE, ItemDisplayTransform::id)

}