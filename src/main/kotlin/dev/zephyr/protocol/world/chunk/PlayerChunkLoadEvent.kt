package dev.zephyr.protocol.world.chunk

import dev.zephyr.event.HandlerListHolder
import org.bukkit.Chunk
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable

class PlayerChunkLoadEvent(player: Player, chunk: Chunk) : PlayerChunkEvent(player, chunk), Cancellable {
    companion object : HandlerListHolder() {

        @JvmStatic
        fun getHandlerList() = handlerList

    }

    @get:JvmName("_isCancelled")
    @set:JvmName("_setCancelled")
    var isCancelled = false

    override fun isCancelled() = isCancelled

    override fun setCancelled(cancel: Boolean) {
        isCancelled = cancel
    }

    override fun getHandlers() = handlerList

}