package dev.zephyr.protocol.entity.world.frame

import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.type.EntityDirection
import dev.zephyr.protocol.entity.type.frame.FrameRotation
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

@KotlinOpens
class ProtocolItemFrame(location: Location, direction: EntityDirection, type: EntityType) : ProtocolEntity(type, direction.ordinal, location) {

    var item by metadata.item(8, MetadataType.ItemStack, ItemStack(Material.AIR))
    var rotation by metadata.item(9, MetadataType.Int, FrameRotation.NONE, FrameRotation::ordinal)

}