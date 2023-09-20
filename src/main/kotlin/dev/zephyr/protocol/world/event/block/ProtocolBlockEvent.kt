package dev.zephyr.protocol.world.event.block

import dev.zephyr.protocol.world.ProtocolBlock
import dev.zephyr.protocol.world.ProtocolStructure
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerEvent

@KotlinOpens
abstract class ProtocolBlockEvent(
    player: Player, val block: ProtocolBlock,
    val structure: ProtocolStructure?, var keep: Boolean = true
) : PlayerEvent(player, true) {

    fun dontKeep() {
        keep = false
    }

    fun keep() {
        keep = true
    }

    fun keeping() {
        if (keep) {
            block.sendSpawnPackets(player)
        }
    }

}