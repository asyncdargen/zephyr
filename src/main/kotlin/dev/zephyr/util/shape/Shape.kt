package dev.zephyr.util.shape

import com.comphenix.protocol.wrappers.BlockPosition
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.util.Vector

interface Shape : Iterable<Block> {

    val world: World
    val chunks: Sequence<Chunk>
    val blocks: Sequence<Block>

    val chunksPositionsBlocks: Map<BlockPosition, List<Block>>
    val chunksBlocks: Map<Chunk, List<Block>>

    fun contains(world: World, x: Double, y: Double, z: Double): Boolean

    fun contains(world: World, point: Vector): Boolean

    operator fun contains(location: Location): Boolean

    operator fun contains(entity: Entity): Boolean

    operator fun contains(block: Block): Boolean

    operator fun contains(shape: Shape): Boolean

    fun clone(world: World = this.world): Shape

}

fun <S : Shape> S.copy(world: World = this.world) = clone(world) as S