package dev.zephyr.protocol.entity.event

import dev.zephyr.event.HandlerListHolder
import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.type.EntityInteract
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerEvent

@KotlinOpens
class PlayerFakeEntityInteractEvent(
    player: Player, val type: EntityInteract,
    val entityId: Int, val entity: ProtocolEntity?
) : PlayerEvent(player, true) {

    companion object : HandlerListHolder() {

        @JvmStatic
        fun getHandlerList() = handlerList

    }

    override fun getHandlers() = handlerList

}