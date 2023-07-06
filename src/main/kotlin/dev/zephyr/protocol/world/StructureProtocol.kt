package dev.zephyr.protocol.world

import dev.zephyr.protocol.PacketPlayInType
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.Protocol
import dev.zephyr.protocol.asChunkSection
import dev.zephyr.protocol.world.event.block.ProtocolBlockClickEvent
import dev.zephyr.protocol.world.event.block.ProtocolBlockDigEvent
import dev.zephyr.protocol.world.event.chunk.PlayerChunkLoadEvent
import dev.zephyr.protocol.world.event.chunk.PlayerChunkUnloadEvent
import dev.zephyr.util.bukkit.afterAsync
import dev.zephyr.util.bukkit.on
import dev.zephyr.util.collection.concurrentSetOf
import dev.zephyr.util.kotlin.safeCast
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerQuitEvent
import kotlin.jvm.optionals.getOrNull

object StructureProtocol {

    val Blocks = concurrentSetOf<ProtocolBlock>()
    val Structures = concurrentSetOf<ProtocolStructure>()

    init {
        on<PlayerQuitEvent> {
            Blocks.forEach { it.destroy(player) }
            Structures.forEach { it.destroy(player) }
        }

        on<PlayerChunkLoadEvent> {
            afterAsync(1) {
                getPlayerBlocks(player)
                    .filter { it.world === player.world && it.chunk.asChunkSection() == it.chunkSection }
                    .forEach { it.sendSpawnPackets(player) }

                getPlayerStructures(player)
                    .filter { it.world === player.world && pointer.section in it.chunksSections }
                    .forEach { it.update((-4..20).map(pointer.section::section), player) }
            }
        }
        on<PlayerChunkUnloadEvent> {
            getPlayerBlocks(player)
                .filter { it.chunk == chunk }
                .forEach { it.sendDestroyPackets(player) }
        }

        Protocol.onSend(PacketPlayOutType.BLOCK_CHANGE, async = true) {
            packet.getMeta<Boolean>("protocol").getOrNull()?.let { return@onSend }
            val position = packet.blockPositionModifier.read(0)

            if (getPlayerBlocks(player).any { it.position == position }
                || getPlayerStructures(player).any { position in it }
            ) isCancelled = true
        }
        Protocol.onReceive(PacketPlayInType.BLOCK_DIG) {
            val position = packet.blockPositionModifier.read(0)

            val block = getPlayerBlocks(player).firstOrNull { it.position == position }
                ?: getPlayerStructures(player).firstNotNullOfOrNull { it[position] }
                ?: return@onReceive
            val structure = block.safeCast<ProtocolStructure.ProtocolStructureBlock>()?.structure
            val type = packet.playerDigTypes.read(0)

            isCancelled = true

            ProtocolBlockDigEvent(player, block, structure, type).callEvent()
        }
        Protocol.onReceive(PacketPlayInType.USE_ITEM) {
            val position = packet.movingBlockPositions.read(0).blockPosition

            val block = getPlayerBlocks(player).firstOrNull { it.position == position }
                ?: getPlayerStructures(player).firstNotNullOfOrNull { it[position] }
                ?: return@onReceive
            val structure = block.safeCast<ProtocolStructure.ProtocolStructureBlock>()?.structure
            val hand = packet.hands.read(0)

            isCancelled = true

            ProtocolBlockClickEvent(player, block, structure, hand).callEvent()
        }
    }

    fun getPlayerBlocks(player: Player) = Blocks
        .asSequence()
        .filter { it.isSpawned(player) }

    fun getPlayerStructures(player: Player) = Structures
        .asSequence()
        .filter { it.isSpawned(player) }

}