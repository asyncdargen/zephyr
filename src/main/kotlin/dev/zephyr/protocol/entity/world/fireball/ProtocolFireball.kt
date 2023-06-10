package dev.zephyr.protocol.entity.world.fireball

import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

@KotlinOpens
class ProtocolFireball(location: Location) : ProtocolEntity(EntityType.FIREBALL, location) {

    var item by metadata.item(8, MetadataType.ItemStack, ItemStack(Material.AIR))

}