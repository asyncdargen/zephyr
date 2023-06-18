package dev.zephyr.extensions.bukkit

import dev.zephyr.protocol.world.PlayerChunks
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player

fun Player.sendOverlay(message: String) = spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))

fun Player.teleportWithoutRotation(destination: Location) =
    teleport(destination.clone().apply { direction = location.direction })

val Player.chunks
    get() = PlayerChunks[this]

fun Player.isChunkLoaded(chunk: Chunk) = chunk in chunks

fun Player.isChunkLoaded(chunkLocation: Location) = isChunkLoaded(chunkLocation.chunk)

fun Player.isChunkLoaded(world: World = this.world, chunkX: Int, chunkZ: Int) =
    isChunkLoaded(world.getChunkAt(chunkX, chunkZ))

val Chunk.loadedByPlayers
    get() = PlayerChunks[this]

fun Chunk.isLoaded(player: Player) = player in loadedByPlayers