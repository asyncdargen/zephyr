package dev.zephyr.protocol.entity.world.display

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.unwrap
import dev.zephyr.protocol.wrap
import dev.zephyr.util.block.WrappedAirBlockData
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.entity.EntityType
import org.bukkit.util.BoundingBox

@KotlinOpens
class ProtocolBlockDisplay(location: Location) : ProtocolDisplay(location, EntityType.BLOCK_DISPLAY) {

    var blockData: BlockData
        set(value) {
            wrappedBlockData = value.wrap()
        }
        get() = wrappedBlockData.unwrap()
    var wrappedBlockData by metadata.item(22, MetadataType.BlockData, WrappedAirBlockData)

    override val boundingBox get() = BoundingBox(.0, .0, .0, scale.x.toDouble(), scale.y.toDouble(), scale.z.toDouble())

}