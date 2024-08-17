package dev.zephyr.protocol.world

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.Protocol
import dev.zephyr.protocol.world.event.chunk.PlayerChunkLoadEvent
import dev.zephyr.protocol.world.event.chunk.PlayerChunkUnloadEvent
import dev.zephyr.util.bukkit.on
import dev.zephyr.util.collection.concurrentHashMapOf
import dev.zephyr.util.collection.concurrentSetOf
import dev.zephyr.util.kotlin.print
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerQuitEvent

object PlayerChunks {

    val PlayersLoadedChunks = concurrentHashMapOf<Player, MutableSet<Position>>()

    init {
        on<PlayerQuitEvent> { unload(player) }
        on<PlayerChangedWorldEvent> { clear(player) }

        Protocol.onSend(PacketPlayOutType.MAP_CHUNK) {
            packet.integers?.apply {
                val chunkX = read(0)
                val chunkZ = read(1)
                isCancelled = !load(player, Position(chunkX, 0, chunkZ))
            }
        }

        Protocol.onSend(PacketPlayOutType.UNLOAD_CHUNK) {
            packet.integers?.apply {
                val chunkX = read(0)
                val chunkZ = read(1)
                unload(player,Position(chunkX, 0, chunkZ))
            }
        }
    }

    fun load(player: Player, position: Position): Boolean {
        val chunkPosition = ChunkPosition(player.world, position)
        val result = PlayerChunkLoadEvent(player, chunkPosition).callEvent()

        if (result) {
            PlayersLoadedChunks.getOrPut(player, ::concurrentSetOf).add(position)
        }

        return result
    }

    fun unload(player: Player, position: Position) {
        val chunkPosition = ChunkPosition(player.world,position)
        if (PlayersLoadedChunks[player]?.remove(position) == true) {
            PlayerChunkUnloadEvent(player, chunkPosition).callEvent()
        }
    }

    fun unload(player: Player) = PlayersLoadedChunks.remove(player)

    fun clear(player: Player) = PlayersLoadedChunks[player]?.clear()

    operator fun get(player: Player) = PlayersLoadedChunks[player] ?: emptySet()

}