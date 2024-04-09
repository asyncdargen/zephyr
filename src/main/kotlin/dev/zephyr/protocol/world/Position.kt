package dev.zephyr.protocol.world

import com.comphenix.protocol.wrappers.BlockPosition
import dev.zephyr.util.bukkit.at
import dev.zephyr.util.bukkit.getBlock
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.util.Vector

@JvmInline
value class Position(val key: Long) {
    constructor(x: Int, y: Int, z: Int) :
            this(((x and 0x3FFFFFF).toLong() shl 38) or ((z and 0x3FFFFFF).toLong() shl 12) or (y and 0xFFF).toLong())

    val x get() = (key shr 38).toInt()
    val y get() = (key shl 52 shr 52).toInt()
    val z get() = (key shl 26 shr 38).toInt()

    val chunk get() = Position(x shr 4, 0, z shr 4)
    val chunkSection get() = Position(x shr 4, y shr 4, z shr 4)

    val minChunkBlock get() = chunkSection.run { Position(x shl 4, y shl 4, z shl 4) }
    val chunkBlockPosition get() = diff(minChunkBlock, -1)

    val chunkBlockPositionIndex
        get() = chunkBlockPosition.run { x shl 8 or (z shl 4) or y }


    val blockPos get() = BlockPosition(x, y, z)
    val vector get() = Vector(x, y, z)

    fun diff(x: Int = 0, y: Int = 0, z: Int = 0) = Position(this.x + x, this.y + y, this.z + z)

    fun diff(position: Position, sign: Int = 1) = diff(position.x * sign, position.y * sign, position.z * sign)

    fun clone(x: Int = this.x, y: Int = this.y, z: Int = this.z) = Position(x, y, z)

    fun toLocation(world: World, yaw: Number = 0, pitch: Number = 0) = world.at(x, y, z, yaw, pitch)

    fun toBlock(world: World) = world.getBlock(x, y, z)

    fun toChunkPosition(world: World) = ChunkPosition(world, this)

    fun toChunkSectionPosition(world: World) = ChunkPosition(world, clone(y = 0))

    operator fun component1() = x
    operator fun component2() = x
    operator fun component3() = x

}

val Chunk.position get() = Position(x, 0, z)

val Block.position get() = Position(x, y, z)

val Location.position get() = Position(blockX, blockY, blockZ)

val Vector.position get() = Position(x.toInt(), y.toInt(), z.toInt())

val BlockPosition.position get() = Position(x, y, z)

data class ChunkPosition(val world: World, val position: Position) {
    constructor(world: World, x: Int, y: Int, z: Int) : this(world, Position(x, y, z))
    constructor(world: World, x: Int, z: Int) : this(world, Position(x, 0, z))

    val x get() = position.x
    val y get() = position.y
    val z get() = position.z

    val chunk get() = world.getChunkAt(x, z)

    fun diff(x: Int, y: Int, z: Int) = ChunkPosition(world, position.diff(x, y, z))

    fun clone(world: World = this.world, x: Int = this.x, y: Int = this.y, z: Int = this.z) =
        ChunkPosition(world, Position(x, y, z))

    fun section(y: Int) = clone(y = y)

    fun full() = if (y == 0) section(0) else this

}

val Chunk.chunkPosition get() = ChunkPosition(world, Position(x, 0, z))

val Location.blockPosition get() = position.blockPos

fun BlockPosition.asLocation(world: World) = world.at(x, y, z)

fun World.chunkPosition(x: Int, y: Int, z: Int) = ChunkPosition(this, x, y, z)

fun World.chunkPosition(x: Int, z: Int) = ChunkPosition(this, x, z)

val Location.chunkToPosition get() = Position(blockX shr 4, 0, blockZ shr 4)
val Location.chunkSectionToPosition get() = Position(blockX shr 4, 0, blockZ shr 4)

val Location.chunkPosition get() = world.chunkPosition(blockX shr 4, blockY shr 4, blockZ shr 4)
val Location.chunkSectionPosition get() = world.chunkPosition(blockX shr 4, 0, blockZ shr 4)

val Block.chunkToPosition get() = location.chunkToPosition
val Block.chunkSectionToPosition get() = location.chunkSectionToPosition

val Block.chunkPosition get() = location.chunkPosition
val Block.chunkSectionPosition get() = location.chunkSectionPosition