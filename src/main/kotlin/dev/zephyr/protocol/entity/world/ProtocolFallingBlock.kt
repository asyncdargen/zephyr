package dev.zephyr.protocol.entity.world

import com.comphenix.protocol.wrappers.BlockPosition
import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.bukkit.state
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolFallingBlock(location: Location, val blockData: BlockData = Material.BARRIER.createBlockData()) : ProtocolEntity(
    EntityType.FALLING_BLOCK, blockData.state, location
) {

    var spawnPosition by metadata.item(8, MetadataType.BlockPosition, BlockPosition.ORIGIN)

}