package dev.zephyr.protocol.world

import dev.zephyr.extensions.bukkit.on
import dev.zephyr.extensions.concurrentHashMapOf
import dev.zephyr.extensions.concurrentSetOf
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.Protocol
import dev.zephyr.protocol.world.chunk.PlayerChunkLoadEvent
import dev.zephyr.protocol.world.chunk.PlayerChunkUnloadEvent
import org.bukkit.Chunk
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerQuitEvent

object PlayerChunks {

    val PlayerLoadedChunks = concurrentHashMapOf<Player, MutableSet<Chunk>>()
    val ChunksLoadedPlayers = concurrentHashMapOf<Chunk, MutableSet<Player>>()

    init {
        on<PlayerQuitEvent> { removeAll(player) }
        on<PlayerChangedWorldEvent> { removeAll(player) }

        Protocol.onSend(PacketPlayOutType.MAP_CHUNK) {
            val chunkX = packet.integers.read(0)
            val chunkZ = packet.integers.read(1)

            val chunk = player.world.getChunkAt(chunkX, chunkZ)
            if (!load(player, chunk)) {
                isCancelled = true
            }
        }
        Protocol.onSend(PacketPlayOutType.UNLOAD_CHUNK) {
            val chunkX = packet.integers.read(0)
            val chunkZ = packet.integers.read(1)

            val chunk = player.world.getChunkAt(chunkX, chunkZ)
            remove(player, chunk)
        }
    }

    fun load(player: Player, chunk: Chunk): Boolean {
        val result = PlayerChunkLoadEvent(player, chunk).callEvent()

        if (result) {
            PlayerLoadedChunks.getOrPut(player, ::concurrentSetOf).add(chunk)
        }

        return result
    }

    fun removeAll(player: Player) {
        ChunksLoadedPlayers.values
            .forEach { it.remove(player) }
        PlayerLoadedChunks.remove(player)
            ?.forEach { PlayerChunkUnloadEvent(player, it).callEvent() }
    }

    fun remove(player: Player, chunk: Chunk) {
        ChunksLoadedPlayers[chunk]?.remove(player)
        if (PlayerLoadedChunks[player]?.remove(chunk) == true) {
            PlayerChunkUnloadEvent(player, chunk).callEvent()
        }
    }

    operator fun get(player: Player) = PlayerLoadedChunks[player] ?: emptySet()

    operator fun get(chunk: Chunk) = ChunksLoadedPlayers[chunk] ?: emptySet()

}