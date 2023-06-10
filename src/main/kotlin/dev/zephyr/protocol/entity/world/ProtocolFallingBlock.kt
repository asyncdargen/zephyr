package dev.zephyr.protocol.entity.world

import com.comphenix.protocol.wrappers.BlockPosition
import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import net.minecraft.world.level.block.Block
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.craftbukkit.v1_19_R3.block.data.CraftBlockData
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolFallingBlock(location: Location, val blockData: BlockData) : ProtocolEntity(
    EntityType.FALLING_BLOCK,
    Block.i((blockData as CraftBlockData).state), location
) {

    var spawnPosition by metadata.item(8, MetadataType.BlockPosition, BlockPosition.ORIGIN)

}