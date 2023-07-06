package dev.zephyr.protocol.world.event.block

import com.comphenix.protocol.wrappers.EnumWrappers
import dev.zephyr.event.HandlerListHolder
import dev.zephyr.protocol.world.ProtocolBlock
import dev.zephyr.protocol.world.ProtocolStructure
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.entity.Player

@KotlinOpens
class ProtocolBlockClickEvent(
    player: Player, block: ProtocolBlock, structure: ProtocolStructure?,
    val hand: EnumWrappers.Hand
) : ProtocolBlockEvent(player, block, structure) {
    companion object : HandlerListHolder() {

        @JvmStatic
        fun getHandlerList() = handlerList

    }

    override fun getHandlers() = handlerList

}