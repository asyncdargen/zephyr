package dev.zephyr.protocol.entity.world

import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import java.util.*

@KotlinOpens
class ProtocolFireworkRocketEntity(location: Location) : ProtocolEntity(EntityType.FIREWORK, location) {

    var fireworkInfo by metadata.item(8, MetadataType.ItemStack, ItemStack(Material.AIR))
    var entityIDUsedFirework by metadata.item(9, MetadataType.IntOptional, OptionalInt.empty())
    var isShotAtAngle by metadata.item(10, MetadataType.Boolean, false)

}