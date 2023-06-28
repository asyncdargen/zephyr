package dev.zephyr.protocol.world

import dev.zephyr.protocol.world.event.chunk.PlayerChunkLoadEvent
import dev.zephyr.protocol.world.event.chunk.PlayerChunkUnloadEvent
import dev.zephyr.util.bukkit.afterAsync
import dev.zephyr.util.bukkit.on
import dev.zephyr.util.collection.concurrentSetOf
import org.bukkit.entity.Player

object StructureProtocol {

    val Blocks = concurrentSetOf<ProtocolBlock>()
    val Structures = concurrentSetOf<ProtocolStructure>()

    init {
        on<PlayerChunkLoadEvent> {
            afterAsync(1) {
                getPlayerBlocks(player)
                    .filter { it.chunk == chunk }
                    .forEach { it.sendSpawnPackets(player) }
            }
        }
        on<PlayerChunkUnloadEvent> {
            getPlayerBlocks(player)
                .filter { it.chunk == chunk }
                .forEach { it.sendDestroyPackets(player) }
        }
    }

    fun getPlayerBlocks(player: Player) = Blocks
        .asSequence()
        .filter { it.isSpawned(player) }

    fun getPlayerStructures(player: Player) = Structures
        .asSequence()
        .filter { it.isSpawned(player) }

}