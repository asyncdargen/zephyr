package dev.zephyr.protocol.world.event.chunk

import dev.zephyr.event.HandlerListHolder
import dev.zephyr.protocol.world.ChunkPointer
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable

@KotlinOpens
class PlayerChunkLoadEvent(player: Player, pointer: ChunkPointer) : PlayerChunkEvent(player, pointer), Cancellable {
    companion object : HandlerListHolder() {

        @JvmStatic
        fun getHandlerList() = handlerList

    }

    private var isCancelled = false

    override fun isCancelled() = isCancelled

    override fun setCancelled(cancel: Boolean) {
        isCancelled = cancel
    }

    override fun getHandlers() = handlerList

}