package dev.zephyr.protocol.world

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.Protocol
import dev.zephyr.protocol.asChunkPointer
import dev.zephyr.protocol.chunkPointer
import dev.zephyr.protocol.world.event.chunk.PlayerChunkLoadEvent
import dev.zephyr.protocol.world.event.chunk.PlayerChunkUnloadEvent
import dev.zephyr.util.bukkit.on
import dev.zephyr.util.collection.concurrentHashMapOf
import dev.zephyr.util.collection.concurrentSetOf
import dev.zephyr.util.numbers.isPositive
import org.bukkit.Chunk
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerQuitEvent
import kotlin.math.absoluteValue

object PlayerChunks {

    val PlayersLoadedChunks = concurrentHashMapOf<Player, MutableSet<ChunkPointer>>()
    val ChunksLoadedPlayers = concurrentHashMapOf<ChunkPointer, MutableSet<Player>>()

    fun initialize() {
        on<PlayerQuitEvent> { removeAll(player) }
        on<PlayerChangedWorldEvent> { removeAll(player) }

        Protocol.onSend(PacketPlayOutType.MAP_CHUNK) {
            val chunkX = packet.integers.read(0)
            val chunkZ = packet.integers.read(1)

            val chunk = player.world.chunkPointer(chunkX, chunkZ)
            if (!load(player, chunk)) {
                isCancelled = true
            }
        }
        Protocol.onSend(PacketPlayOutType.UNLOAD_CHUNK) {
            val chunkX = packet.integers.read(0)
            val chunkZ = packet.integers.read(1)

            val chunk = player.world.chunkPointer(chunkX, chunkZ)
            remove(player, chunk)
        }
    }

    fun load(player: Player, pointer: ChunkPointer): Boolean {
        val result = PlayerChunkLoadEvent(player, pointer).callEvent()

        if (result) {
            PlayersLoadedChunks.getOrPut(player, ::concurrentSetOf).add(pointer)
            ChunksLoadedPlayers.getOrPut(pointer, ::concurrentSetOf).add(player)
        }

        return result
    }

    fun removeAll(player: Player) {
        ChunksLoadedPlayers.values
            .forEach { it.remove(player) }
        PlayersLoadedChunks.remove(player)
            ?.forEach { PlayerChunkUnloadEvent(player, it).callEvent() }
    }

    fun remove(player: Player, pointer: ChunkPointer) {
        ChunksLoadedPlayers[pointer]?.remove(player)
        if (PlayersLoadedChunks[player]?.remove(pointer) == true) {
            PlayerChunkUnloadEvent(player, pointer).callEvent()
        }
    }

    operator fun get(player: Player) = PlayersLoadedChunks[player] ?: emptySet()

    operator fun get(pointer: ChunkPointer) = ChunksLoadedPlayers[pointer.full()] ?: emptySet()

    operator fun get(chunk: Chunk) = get(chunk.asChunkPointer())

}

data class ChunkPointer(val world: World, val section: ChunkSection) {

    constructor(world: World, x: Int, y: Int, z: Int) : this(world, ChunkSection(x, y, z))
    constructor(world: World, x: Int, z: Int) : this(world, x, 0, z)

    val x by section::x
    val y by section::y
    val z by section::z

    val chunk get() = world.getChunkAt(x, z)

    fun full() = if (y == 0) this else ChunkPointer(world, x, z)

}

@JvmInline
value class ChunkSection(val data: Long) {

    constructor(x: Int, y: Int, z: Int) : this((x.compress(21) shl 28) or (y.compress(5) shl 22) or z.compress(21))
    constructor(x: Int, z: Int) : this(x, 0, z)

    val x get() = (data shr 28).decompress(21, MaskXZ)
    val y get() = (data shr 22).decompress(5, MaskY)
    val z get() = data.decompress(21, MaskXZ)

    fun pointer(world: World) = world.chunkPointer(x, y, z)

    fun section(y: Int) = ChunkSection(x, y, z)

    fun full() = if (y == 0) this else ChunkSection(x, z)

    companion object {

        val MaskXZ = 0x3FFFFF
        val MaskY = 0x3F

        inline fun Int.compress(diff: Int) = (if (isPositive) 0 else 1 shl diff) + absoluteValue.toLong()

        inline fun Long.decompress(diff: Int, mask: Int) = (toInt() and mask).run {
            (if (((this shr diff) and 0x1) == 1) -1 else 1) * (this and (mask shr 1))
        }

    }

}