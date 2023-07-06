package dev.zephyr.protocol

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.reflect.StructureModifier
import com.comphenix.protocol.wrappers.BlockPosition
import dev.zephyr.protocol.world.ChunkPointer
import dev.zephyr.protocol.world.ChunkSection
import dev.zephyr.util.bukkit.at
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block

typealias PacketPlayOutType = PacketType.Play.Server
typealias PacketPlayInType = PacketType.Play.Client

fun <T> StructureModifier<T>.edit(index: Int, mapper: (T) -> T) = write(index, read(index).run(mapper))

fun Location.asBlockPosition() = BlockPosition(blockX, blockY, blockZ)

fun BlockPosition.asLocation(world: World) = world.at(x, y, z)

fun World.chunkPointer(x: Int, y: Int, z: Int) = ChunkPointer(this, x, y, z)

fun World.chunkPointer(x: Int, z: Int) = ChunkPointer(this, x, z)

fun Location.asChunkPointer() = world.chunkPointer(x.toInt() shl 4, 0, z.toInt() shl 4)

fun Location.asChunkSectionPointer() = world.chunkPointer(x.toInt() shr 4, y.toInt() shr 4, z.toInt() shr 4)

fun Location.asChunkSection() = ChunkSection(x.toInt() shr 4, y.toInt() shr 4, z.toInt() shr 4)

fun Block.getChunkPointer() = location.asChunkPointer()

fun Block.getChunkSectionPointer() = location.asChunkSectionPointer()

fun Block.getChunkSection() = location.asChunkSection()

fun BlockPosition.getChunkSection() = ChunkSection(x shr 4, y shr 4, z shr 4)

fun Chunk.asChunkPointer() = world.chunkPointer(x, z)

fun Chunk.asChunkSectionPointer(y: Int) = world.chunkPointer(x, y, z)

fun Chunk.asChunkSection() = ChunkSection(x, z)