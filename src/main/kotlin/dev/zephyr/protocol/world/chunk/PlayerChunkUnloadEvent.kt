package dev.zephyr.protocol.world.chunk

import dev.zephyr.event.HandlerListHolder
import org.bukkit.Chunk
import org.bukkit.entity.Player

class PlayerChunkUnloadEvent(player: Player, chunk: Chunk) : PlayerChunkEvent(player, chunk) {
    companion object : HandlerListHolder() {

        @JvmStatic
        fun getHandlerList() = handlerList

    }

    override fun getHandlers() = handlerList

}