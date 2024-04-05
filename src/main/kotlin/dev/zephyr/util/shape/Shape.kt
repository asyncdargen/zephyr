package dev.zephyr.util.shape

import dev.zephyr.protocol.world.ChunkPosition
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.util.Vector

interface Shape : Iterable<Location> {

    val world: World
    val chunks: Sequence<Chunk>
    val blocks: Sequence<Block>
    val entities: Sequence<Entity>

    val chunksPositionsBlocks: Map<ChunkPosition, List<Block>>
    val chunksBlocks: Map<Chunk, List<Block>>

    fun contains(world: World, x: Double, y: Double, z: Double): Boolean

    fun contains(world: World, point: Vector): Boolean

    operator fun contains(location: Location): Boolean

    operator fun contains(entity: Entity): Boolean

    operator fun contains(block: Block): Boolean

    operator fun contains(shape: Shape): Boolean

    fun expandVertical(y: Double): Shape

    fun expandVertical(minY: Double, maxY: Double): Shape

    fun expandHorizontal(value: Double): Shape

    fun expandHorizontal(x: Double, z: Double): Shape

    fun expandHorizontal(minX: Double, minZ: Double, maxX: Double, maxZ: Double): Shape

    fun expand(value: Double): Shape

    fun expand(x: Double, y: Double, z: Double): Shape

    fun expand(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double): Shape

    fun diff(value: Double): Shape

    fun diff(x: Double, y: Double, z: Double): Shape

    fun clone(world: World = this.world): Shape

}

@Suppress("Unchecked_Cast")
fun <S : Shape> S.copy(world: World = this.world) = clone(world) as S