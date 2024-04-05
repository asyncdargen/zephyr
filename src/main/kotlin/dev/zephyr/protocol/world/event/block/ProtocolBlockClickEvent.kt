package dev.zephyr.protocol.world.event.block

import com.comphenix.protocol.wrappers.EnumWrappers
import dev.zephyr.event.HandlerListHolder
import dev.zephyr.protocol.world.Position
import dev.zephyr.protocol.world.ProtocolStructure
import dev.zephyr.protocol.world.block.ProtocolBlock
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.entity.Player

@KotlinOpens
class ProtocolBlockClickEvent(
    player: Player, position: Position,
    block: ProtocolBlock?, structure: ProtocolStructure?,
    val hand: EnumWrappers.Hand
) : ProtocolBlockEvent(player, position, block, structure) {
    companion object : HandlerListHolder() {

        @JvmStatic
        fun getHandlerList() = handlerList

    }

    override fun getHandlers() = handlerList

}