package dev.zephyr.protocol.world

import dev.zephyr.protocol.PacketPlayInType
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.Protocol
import dev.zephyr.protocol.packet.world.PacketBlockAck
import dev.zephyr.protocol.world.block.ProtocolBlock
import dev.zephyr.protocol.world.event.block.ProtocolBlockClickEvent
import dev.zephyr.protocol.world.event.block.ProtocolBlockDigEvent
import dev.zephyr.protocol.world.event.chunk.PlayerChunkLoadEvent
import dev.zephyr.util.bukkit.afterAsync
import dev.zephyr.util.bukkit.call
import dev.zephyr.util.bukkit.on
import dev.zephyr.util.collection.concurrentSetOf
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
                    .filter { it.world === player.world && position.position == it.chunkSectionPosition }
                    .forEach { it.sendSpawnPackets(player) }

                val sections = (-4..20).map { position.position.clone(y = it) }

                getPlayerStructures(player)
                    .filter { it.world === player.world && sections.any { section -> section in it.chunksSections } }
                    .forEach { it.update(sections, player) }
            }
        }

        Protocol.onSend(PacketPlayOutType.BLOCK_CHANGE, async = true) {
            packet.getMeta<Boolean>("protocol")
                .getOrNull()
                ?.let { return@onSend }
            val position = packet.blockPositionModifier.read(0).position

            if (getPlayerBlocks(player).any { it.position == position }
                || getPlayerStructures(player).any { position in it }
            ) isCancelled = true
        }
//        Protocol.onSend(PacketPlayOutType.MULTI_BLOCK_CHANGE, async = true) {
//            packet.getMeta<Boolean>("protocol")
//                .getOrNull()
//                ?.let { return@onSend }
//            val chunk = packet.sectionPositions.read(0).asChunkSection()
//
//            if (getPlayerBlocks(player).any { it.chunkSection == chunk }
//                || getPlayerStructures(player).any { chunk in it.chunksSections }
//            ) {
//                afterAsync {
//
//                }
//            }
//        }
        Protocol.onReceive(PacketPlayInType.BLOCK_DIG, async = true) {
            val position = packet.blockPositionModifier.read(0).position

            val block = getPlayerBlocks(player).firstOrNull { it.position == position }
            val structure = getPlayerStructures(player).firstOrNull { position in it }
            block ?: structure ?: return@onReceive

            val type = packet.playerDigTypes.read(0)

            isCancelled = true

            PacketBlockAck(packet.integers.read(0)).send(player)

            ProtocolBlockDigEvent(player, position, block, structure, type).call().keeping()
        }
        Protocol.onReceive(PacketPlayInType.USE_ITEM, async = true) {
            val position = packet.movingBlockPositions.read(0).blockPosition.position

            val block = getPlayerBlocks(player).firstOrNull { it.position == position }
            val structure = getPlayerStructures(player).firstOrNull { position in it }
            block ?: structure ?: return@onReceive

            val hand = packet.hands.read(0)

            isCancelled = true

            ProtocolBlockClickEvent(player, position, block, structure, hand).call().keeping()
        }
    }

    fun getPlayerBlocks(player: Player) = Blocks
        .asSequence()
        .filter { it.isSpawned(player) }

    fun getPlayerStructures(player: Player) = Structures
        .asSequence()
        .filter { it.isSpawned(player) }

}