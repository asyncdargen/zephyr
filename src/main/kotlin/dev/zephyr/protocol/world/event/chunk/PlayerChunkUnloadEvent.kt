package dev.zephyr.protocol.world.event.chunk

import dev.zephyr.event.HandlerListHolder
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Chunk
import org.bukkit.entity.Player

@KotlinOpens
class PlayerChunkUnloadEvent(player: Player, chunk: Chunk) : PlayerChunkEvent(player, chunk) {
    companion object : HandlerListHolder() {

        @JvmStatic
        fun getHandlerList() = handlerList

    }

    override fun getHandlers() = handlerList

}