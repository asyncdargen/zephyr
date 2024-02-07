package dev.zephyr.util.shape

import dev.zephyr.protocol.getChunkSectionPointer
import dev.zephyr.protocol.world.ChunkPointer
import dev.zephyr.util.bukkit.at
import dev.zephyr.util.bukkit.diff
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.util.Vector
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

@KotlinOpens
class Cuboid protected constructor(override val world: World, val minPoint: Vector, val maxPoint: Vector) : Shape {

    companion object {

        fun at(world: World, x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double) = Cuboid(
            world,
            Vector(floor(min(x1, x2)), floor(min(y1, y2)), floor(min(z1, z2))),
            Vector(floor(max(x1, x2)), floor(max(y1, y2)), floor(max(z1, z2)))
        )

        fun at(world: World, firstPoint: Vector, secondPoint: Vector) =
            at(world, firstPoint.x, firstPoint.y, firstPoint.z, secondPoint.x, secondPoint.y, secondPoint.z)

        fun at(firstPoint: Location, secondPoint: Location) =
            at(firstPoint.world, firstPoint.toVector(), secondPoint.toVector())

    }

    val center
        get() = world.at(
            (minPoint.x + maxPoint.x) / 2.0,
            (minPoint.y + maxPoint.y) / 2.0,
            (minPoint.z + maxPoint.z) / 2.0
        )
    val minLocation get() = minPoint.toLocation(world)
    val maxLocation get() = maxPoint.toLocation(world)

    val widthX get() = maxPoint.blockX - minPoint.blockX + 1
    val height get() = maxPoint.blockY - minPoint.blockY + 1
    val widthZ get() = maxPoint.blockZ - minPoint.blockZ + 1
    val size get() = Vector(widthX, height, widthZ)

    val rangeX = minPoint.x..<(maxPoint.blockX + 1.0)
    val rangeY = minPoint.y..<(maxPoint.blockY + 1.0)
    val rangeZ = minPoint.z..<(maxPoint.blockZ + 1.0)

    override val chunks: Sequence<Chunk>
        get() = blocks.map(Block::getChunk).distinct()
    override val blocks: Sequence<Block>
        get() = asSequence().map(Location::getBlock)
    override val entities: Sequence<Entity>
        get() = chunks.map(Chunk::getEntities).map(Array<Entity>::toList).flatten()

    override val chunksPositionsBlocks: Map<ChunkPointer, List<Block>>
        get() = blocks.groupBy(Block::getChunkSectionPointer)
    override val chunksBlocks: Map<Chunk, List<Block>>
        get() = blocks.groupBy(Block::getChunk)

    override fun contains(world: World, x: Double, y: Double, z: Double) =
        this.world === world && x in rangeX && y in rangeY && z in rangeZ

    override fun contains(world: World, point: Vector) =
        contains(world, point.x, point.y, point.z)

    override fun contains(location: Location) =
        contains(world, location.x, location.y, location.z)

    override fun contains(entity: Entity) =
        contains(entity.location)

    override fun contains(block: Block) =
        contains(block.location)

    override fun contains(shape: Shape) =
        shape.all { contains(it) }

    override fun expandVertical(y: Double) =
        expandVertical(y, y)

    override fun expandVertical(minY: Double, maxY: Double) =
        expand(.0, minY, .0, .0, maxY, .0)

    override fun expandHorizontal(value: Double) =
        expandVertical(value, value)

    override fun expandHorizontal(x: Double, z: Double) =
        expandHorizontal(x, x, z, z)

    override fun expandHorizontal(minX: Double, minZ: Double, maxX: Double, maxZ: Double) =
        expand(minX, .0, minZ, maxX, .0, maxZ)

    override fun expand(value: Double) =
        expand(value, value, value)

    override fun expand(x: Double, y: Double, z: Double) =
        expand(x, y, z, x, y, z)

    override fun expand(minX: Double, minY: Double, minZ: Double, maxX: Double, maxY: Double, maxZ: Double) =
        at(minLocation.diff(-minX, -minY, -minZ), maxLocation.diff(maxX, maxY, maxZ))

    override fun diff(value: Double) =
        diff(value, value, value)

    override fun diff(x: Double, y: Double, z: Double) =
        expand(-x, -y, -z, x, y, z)

    override fun clone(world: World) =
        Cuboid(world, minPoint.clone(), maxPoint.clone())

    override fun iterator() = CuboidIterator()

    inner class CuboidIterator : Iterator<Location> {

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

        override fun next() = world.at(minPoint.x + position.x, minPoint.y + position.y, minPoint.z + position.z)
            .apply { step() }

    }

}