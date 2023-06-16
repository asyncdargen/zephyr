package dev.zephyr.util.shape

import com.comphenix.protocol.wrappers.BlockPosition
import dev.zephyr.extensions.bukkit.getBlock
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.util.Vector
import kotlin.math.max
import kotlin.math.min

@KotlinOpens
class Cuboid(override val world: World, val minPoint: Vector, val maxPoint: Vector) : Shape {

    companion object {

        fun at(world: World, x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double) =
            Cuboid(world, Vector(min(x1, x2), min(y1, y2), min(z1, z2)), Vector(max(x1, x2), max(y1, y2), max(z1, z2)))

        fun at(world: World, firstPoint: Vector, secondPoint: Vector) =
            at(world, firstPoint.x, firstPoint.y, firstPoint.z, secondPoint.x, secondPoint.y, secondPoint.z)

        fun at(firstPoint: Location, secondPoint: Location) =
            at(firstPoint.world, firstPoint.toVector(), secondPoint.toVector())

    }

    val widthX get() = maxPoint.blockX - minPoint.blockX + 1
    val height get() = maxPoint.blockY - minPoint.blockY + 1
    val widthZ get() = maxPoint.blockZ - minPoint.blockZ + 1

    override val chunks: Sequence<Chunk>
        get() = blocks.map(Block::getChunk).distinct()
    override val blocks: Sequence<Block>
        get() = asSequence()

    override val chunksPositionsBlocks: Map<BlockPosition, List<Block>>
        get() = blocks.groupBy { BlockPosition(it.x shr 4, it.x shr 4, it.x shr 4) }
    override val chunksBlocks: Map<Chunk, List<Block>>
        get() = blocks.groupBy(Block::getChunk)

    override fun contains(world: World, x: Double, y: Double, z: Double) =
        this.world === world
                && x in minPoint.x..maxPoint.x
                && x in minPoint.y..maxPoint.y
                && x in minPoint.z..maxPoint.z

    override fun contains(world: World, point: Vector) =
        contains(world, point.x, point.y, point.z)

    override fun contains(location: Location) =
        contains(world, location.toVector())

    override fun contains(entity: Entity) =
        contains(entity.location)

    override fun contains(block: Block) =
        contains(block.location)

    override fun contains(shape: Shape) =
        shape.all { contains(it) }

    override fun clone(world: World) =
        Cuboid(world, minPoint.clone(), maxPoint.clone())

    override fun iterator() = CuboidIterator()

    inner class CuboidIterator : Iterator<Block> {

        private val position = Vector()

        private fun step() {
            if (++position.x >= widthX) {
                position.x = .0
                if (++position.z >= widthZ) {
                    position.z = .0
                    position.y++
                }
            }
        }

        override fun hasNext() = position.x < widthX && position.y < height && position.z < widthZ

        override fun next() = world.getBlock(minPoint.x + position.x, minPoint.y + position.y, minPoint.z + position.z)
            .apply { step() }

    }

}