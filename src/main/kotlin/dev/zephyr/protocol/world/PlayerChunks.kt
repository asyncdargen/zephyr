package dev.zephyr.protocol.world

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.Protocol
import dev.zephyr.protocol.world.event.chunk.PlayerChunkLoadEvent
import dev.zephyr.protocol.world.event.chunk.PlayerChunkUnloadEvent
import dev.zephyr.util.bukkit.on
import dev.zephyr.util.collection.concurrentHashMapOf
import dev.zephyr.util.collection.concurrentSetOf
import org.bukkit.Chunk
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerQuitEvent

object PlayerChunks {

    val PlayersLoadedChunks = concurrentHashMapOf<Player, MutableSet<Chunk>>()
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
            PlayersLoadedChunks.getOrPut(player, ::concurrentSetOf).add(chunk)
            ChunksLoadedPlayers.getOrPut(chunk, ::concurrentSetOf).add(player)
        }

        return result
    }

    fun removeAll(player: Player) {
        ChunksLoadedPlayers.values
            .forEach { it.remove(player) }
        PlayersLoadedChunks.remove(player)
            ?.forEach { PlayerChunkUnloadEvent(player, it).callEvent() }
    }

    fun remove(player: Player, chunk: Chunk) {
        ChunksLoadedPlayers[chunk]?.remove(player)
        if (PlayersLoadedChunks[player]?.remove(chunk) == true) {
            PlayerChunkUnloadEvent(player, chunk).callEvent()
        }
    }

    operator fun get(player: Player) = PlayersLoadedChunks[player] ?: emptySet()

    operator fun get(chunk: Chunk) = ChunksLoadedPlayers[chunk] ?: emptySet()

}