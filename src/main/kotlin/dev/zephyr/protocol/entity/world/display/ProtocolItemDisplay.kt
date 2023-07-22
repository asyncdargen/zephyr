package dev.zephyr.protocol.entity.world.display

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.type.display.ItemDisplayTransform
import dev.zephyr.util.item.item
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

@KotlinOpens
class ProtocolItemDisplay(location: Location, item: ItemStack = item(Material.AIR)) :
    ProtocolDisplay(location, EntityType.ITEM_DISPLAY) {

    var item by metadata.item(22, MetadataType.ItemStack, item)
    var transform by metadata.item(23, MetadataType.Byte, ItemDisplayTransform.NONE, ItemDisplayTransform::id)

}