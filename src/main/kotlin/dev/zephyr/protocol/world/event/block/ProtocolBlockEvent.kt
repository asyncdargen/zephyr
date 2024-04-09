package dev.zephyr.protocol.world.event.block

import dev.zephyr.protocol.world.Position
import dev.zephyr.protocol.world.block.ProtocolBlock
import dev.zephyr.protocol.world.structure.ProtocolStructure
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerEvent

@KotlinOpens
abstract class ProtocolBlockEvent(
    player: Player, val position: Position,

    val block: ProtocolBlock?, val structure: ProtocolStructure?,

    var keep: Boolean = true
) : PlayerEvent(player, true) {

    fun dontKeep() {
        keep = false
    }

    fun keep() {
        keep = true
    }

    fun keeping() {
        if (keep) {
            block?.sendSpawnPackets(player)
            structure?.apply { position.sendBlockData() }
//            if (type == EnumWrappers.PlayerDigType.STOP_DESTROY_BLOCK) {
//                PacketBlockAck(packet.integers.read(0)).send(player)
//            }
        }
    }

}