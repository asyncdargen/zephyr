package dev.zephyr.protocol.world.event.block

import com.comphenix.protocol.wrappers.EnumWrappers
import dev.zephyr.event.HandlerListHolder
import dev.zephyr.protocol.packet.world.PacketBlockAck
import dev.zephyr.protocol.world.Position
import dev.zephyr.protocol.world.block.ProtocolBlock
import dev.zephyr.protocol.world.structure.ProtocolStructure
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.entity.Player

@KotlinOpens
class ProtocolBlockDigEvent(
    player: Player, position: Position,
    block: ProtocolBlock?, structure: ProtocolStructure?,
    val type: EnumWrappers.PlayerDigType, val sequence: Int
) : ProtocolBlockEvent(player, position, block, structure) {
    companion object : HandlerListHolder() {

        @JvmStatic
        fun getHandlerList() = handlerList

    }

    override fun getHandlers() = handlerList

    override fun keeping() {
        if (keep) {
            PacketBlockAck(sequence).send(player)
        }
    }

}