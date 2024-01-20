package dev.zephyr.util.bukkit

import com.comphenix.protocol.wrappers.BlockPosition
import dev.zephyr.protocol.asChunkPointer
import dev.zephyr.protocol.chunkPointer
import dev.zephyr.protocol.getChunkPointer
import dev.zephyr.protocol.world.ChunkPointer
import dev.zephyr.protocol.world.ChunkSection
import dev.zephyr.protocol.world.PlayerChunks
import dev.zephyr.util.component.toComponent
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer
import org.bukkit.entity.Player

fun players() = Bukkit.getOnlinePlayers()

fun Player.craft() = this as CraftPlayer

fun Player.sendOverlay(message: String) = sendActionBar(message.toComponent())

fun Player.teleportWithoutRotation(destination: Location) =
    teleport(destination.copyDirection(location))

val Player.loadedChunks
    get() = PlayerChunks[this]

fun Player.isChunkLoaded(pointer: ChunkPointer) = pointer.full() in loadedChunks

fun Player.isChunkLoaded(section: ChunkSection) = isChunkLoaded(section.pointer(world))

fun Player.isChunkLoaded(chunk: Chunk) = isChunkLoaded(chunk.asChunkPointer())

fun Player.isChunkLoaded(chunkLocation: Location) = isChunkLoaded(chunkLocation.asChunkPointer())

fun Player.isChunkLoaded(block: Block) = isChunkLoaded(block.location)

fun Player.isChunkLoaded(world: World = this.world, chunkX: Int, chunkZ: Int) =
    isChunkLoaded(world.chunkPointer(chunkX, chunkZ))

fun Player.isChunkLoaded(world: World = this.world, chunkSection: BlockPosition) =
    isChunkLoaded(world, chunkSection.x, chunkSection.z)

val ChunkPointer.loadedByPlayers
    get() = PlayerChunks[this]

val Chunk.loadedByPlayers
    get() = asChunkPointer().loadedByPlayers

fun ChunkPointer.isLoaded(player: Player) = player in loadedByPlayers

fun Chunk.isLoaded(player: Player) = asChunkPointer().isLoaded(player)

fun Block.isLoaded(player: Player) = getChunkPointer().isLoaded(player)
