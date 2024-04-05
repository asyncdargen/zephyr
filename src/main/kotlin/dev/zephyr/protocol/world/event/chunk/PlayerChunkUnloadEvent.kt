package dev.zephyr.protocol.world.event.chunk

import dev.zephyr.event.HandlerListHolder
import dev.zephyr.protocol.world.ChunkPosition
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.entity.Player

@KotlinOpens
class PlayerChunkUnloadEvent(player: Player, position: ChunkPosition) : PlayerChunkEvent(player, position) {
    companion object : HandlerListHolder() {

        @JvmStatic
        fun getHandlerList() = handlerList

    }

    override fun getHandlers() = handlerList

}