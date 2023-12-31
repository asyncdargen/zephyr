package dev.zephyr.protocol

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.reflect.StructureModifier
import com.comphenix.protocol.wrappers.BlockPosition
import com.comphenix.protocol.wrappers.WrappedBlockData
import dev.zephyr.protocol.world.ChunkPointer
import dev.zephyr.protocol.world.ChunkSection
import dev.zephyr.util.bukkit.at
import dev.zephyr.util.kotlin.cast
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.craftbukkit.v1_19_R3.block.data.CraftBlockData

typealias PacketPlayOutType = PacketType.Play.Server
typealias PacketPlayInType = PacketType.Play.Client

fun <T> StructureModifier<T>.edit(index: Int, mapper: (T) -> T) = write(index, read(index).run(mapper))

fun Location.asBlockPosition() = BlockPosition(blockX, blockY, blockZ)

fun BlockPosition.asLocation(world: World) = world.at(x, y, z)

fun World.chunkPointer(x: Int, y: Int, z: Int) = ChunkPointer(this, x, y, z)

fun World.chunkPointer(x: Int, z: Int) = ChunkPointer(this, x, z)

fun Location.asChunkPointer() = world.chunkPointer(blockX shr 4, 0, blockZ shr 4)

fun Location.asChunkSectionPointer() = world.chunkPointer(blockX shr 4, blockY shr 4, blockZ shr 4)

fun Location.asChunkSection() = ChunkSection(blockX shr 4, blockY shr 4, blockZ shr 4)

fun Block.getChunkPointer() = location.asChunkPointer()

fun Block.getChunkSectionPointer() = location.asChunkSectionPointer()

fun Block.getChunkSection() = location.asChunkSection()

fun BlockPosition.getChunkSection() = ChunkSection(x shr 4, y shr 4, z shr 4)

fun Chunk.asChunkPointer() = world.chunkPointer(x, z)

fun Chunk.asChunkSectionPointer(y: Int) = world.chunkPointer(x, y, z)

fun Chunk.asChunkSection() = ChunkSection(x, z)

fun BlockPosition.asChunkSection() = ChunkSection(x, y, z)

fun BlockData.wrap() = WrappedBlockData.createData(this)

fun WrappedBlockData.unwrap() = CraftBlockData.fromData(handle.cast())