package dev.zephyr.protocol.entity.world.display

import com.comphenix.protocol.wrappers.WrappedBlockData
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.entity.EntityType
import org.bukkit.util.BoundingBox

@KotlinOpens
class ProtocolBlockDisplay(location: Location) : ProtocolDisplay(location, EntityType.BLOCK_DISPLAY) {

    var blockData: BlockData by metadata.item(22, MetadataType.BlockData, Material.AIR.createBlockData(), WrappedBlockData::createData)

    override val boundingBox get() = BoundingBox(.0, .0, .0, scale.x.toDouble(), scale.y.toDouble(), scale.z.toDouble())

}