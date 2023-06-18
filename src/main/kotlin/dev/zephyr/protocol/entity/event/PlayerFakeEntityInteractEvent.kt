package dev.zephyr.protocol.entity.event

import dev.zephyr.event.HandlerListHolder
import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.type.EntityInteract
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerEvent

class PlayerFakeEntityInteractEvent(
    player: Player, val type: EntityInteract,
    val entityId: Int, val entity: ProtocolEntity?
) : PlayerEvent(player) {

    companion object : HandlerListHolder() {

        @JvmStatic
        fun getHandlerList() = handlerList

    }

    override fun getHandlers() = handlerList

}