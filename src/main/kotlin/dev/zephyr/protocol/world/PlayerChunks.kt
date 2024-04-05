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

    val PlayersLoadedChunks = concurrentHashMapOf<Player, MutableSet<Position>>()
    val ChunksLoadedPlayers = concurrentHashMapOf<ChunkPosition, MutableSet<Player>>()

    init {
        on<PlayerQuitEvent> { removeAll(player) }
        on<PlayerChangedWorldEvent> { removeAll(player) }

        Protocol.onSend(PacketPlayOutType.MAP_CHUNK) {
            val chunkX = packet.integers.read(0)
            val chunkZ = packet.integers.read(1)

            val chunk = ChunkPosition(player.world, Position(chunkX, 0, chunkZ))
            if (!load(player, chunk)) {
                isCancelled = true
            }
        }
        Protocol.onSend(PacketPlayOutType.UNLOAD_CHUNK) {
            val chunkX = packet.integers.read(0)
            val chunkZ = packet.integers.read(1)

            val chunk = ChunkPosition(player.world, Position(chunkX, 0, chunkZ))
            remove(player, chunk)
        }
    }

    fun load(player: Player, position: ChunkPosition): Boolean {
        val result = PlayerChunkLoadEvent(player, position).callEvent()

        if (result) {
            PlayersLoadedChunks.getOrPut(player, ::concurrentSetOf).add(position.position)
            ChunksLoadedPlayers.getOrPut(position, ::concurrentSetOf).add(player)
        }

        return result
    }

    fun removeAll(player: Player) {
        ChunksLoadedPlayers.values
            .forEach { it.remove(player) }
        PlayersLoadedChunks.remove(player)
            ?.forEach { PlayerChunkUnloadEvent(player, it.toChunkPosition(player.world)).callEvent() }
    }

    fun remove(player: Player, position: ChunkPosition) {
        ChunksLoadedPlayers[position]?.remove(player)
        if (PlayersLoadedChunks[player]?.remove(position.position) == true) {
            PlayerChunkUnloadEvent(player, position).callEvent()
        }
    }

    operator fun get(player: Player) = PlayersLoadedChunks[player] ?: emptySet()

    operator fun get(pointer: ChunkPosition) = ChunksLoadedPlayers[pointer.full()] ?: emptySet()

    operator fun get(chunk: Chunk) = get(chunk.chunkPosition)

}