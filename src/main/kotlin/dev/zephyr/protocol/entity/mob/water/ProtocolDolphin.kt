package dev.zephyr.protocol.entity.mob.water

import com.comphenix.protocol.wrappers.BlockPosition
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolDolphin(location: Location) : ProtocolWaterAnimal(EntityType.DOLPHIN, location) {

    var treasurePosition by metadata.item(16, MetadataType.BlockPosition, BlockPosition.ORIGIN)
    var hasFish by metadata.item(17, MetadataType.Boolean, false)
    var moistureLevel by metadata.item(18, MetadataType.Int, 2400)

}