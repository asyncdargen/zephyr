package dev.zephyr.util.bukkit

import dev.zephyr.protocol.world.PlayerChunks
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer
import org.bukkit.entity.Player

fun Player.craft() = this as CraftPlayer

fun Player.sendOverlay(message: String) = sendActionBar(message.toComponent())

fun Player.teleportWithoutRotation(destination: Location) =
    teleport(destination.clone().apply { direction = location.direction })

val Player.loadedChunks
    get() = PlayerChunks[this]

fun Player.isChunkLoaded(chunk: Chunk) = chunk in loadedChunks

fun Player.isChunkLoaded(chunkLocation: Location) = isChunkLoaded(chunkLocation.chunk)

fun Player.isChunkLoaded(world: World = this.world, chunkX: Int, chunkZ: Int) =
    isChunkLoaded(world.getChunkAt(chunkX, chunkZ))

val Chunk.loadedByPlayers
    get() = PlayerChunks[this]

fun Chunk.isLoaded(player: Player) = player in loadedByPlayers