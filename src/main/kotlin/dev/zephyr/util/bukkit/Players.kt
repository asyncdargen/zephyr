package dev.zephyr.util.bukkit

import com.comphenix.protocol.wrappers.BlockPosition
import dev.zephyr.protocol.world.*
import dev.zephyr.util.component.toComponent
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer
import org.bukkit.entity.Player

fun Player.isTracked(player: Player) = player === this || player in trackedPlayers

fun players() = Bukkit.getOnlinePlayers()

fun player(name: String) = Bukkit.getPlayer(name)

fun Player.craft() = this as CraftPlayer

fun Player.sendOverlay(message: String) = sendActionBar(message.toComponent())

fun Player.teleportWithoutRotation(destination: Location) =
    teleport(destination.copyDirection(location))

val Player.loadedChunks
    get() = PlayerChunks[this]

fun Player.isChunkLoaded(pointer: ChunkPosition) = isChunkLoaded(pointer.world, pointer.position)

fun Player.isChunkLoaded(world: World, section: Position) = this.world === world && (if (section.y == 0) section else section.clone(y = 0)) in loadedChunks

fun Player.isChunkLoaded(chunk: Chunk) = isChunkLoaded(chunk.chunkPosition)

fun Player.isChunkLoaded(location: Location) = isChunkLoaded(world, location.chunkToPosition)

fun Player.isChunkLoaded(block: Block) = isChunkLoaded(block.location)

fun Player.isChunkLoaded(world: World = this.world, chunkX: Int, chunkZ: Int) =
    isChunkLoaded(world.chunkPosition(chunkX, chunkZ))

fun Player.isChunkLoaded(world: World = this.world, chunkSection: BlockPosition) =
    isChunkLoaded(world, chunkSection.x, chunkSection.z)

val ChunkPosition.loadedByPlayers
    get() = PlayerChunks[this]

val Chunk.loadedByPlayers
    get() = chunkPosition.loadedByPlayers

fun ChunkPosition.isLoaded(player: Player) = player in loadedByPlayers

fun Chunk.isLoaded(player: Player) = chunkPosition.isLoaded(player)

fun Block.isLoaded(player: Player) = chunkPosition.isLoaded(player)
