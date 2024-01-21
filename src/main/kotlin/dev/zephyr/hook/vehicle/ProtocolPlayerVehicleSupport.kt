package dev.zephyr.hook.vehicle

import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.ProtocolVehicle
import dev.zephyr.util.bukkit.everyAsync
import dev.zephyr.util.bukkit.on
import dev.zephyr.util.collection.concurrentHashMapOf
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerTeleportEvent

object ProtocolPlayerVehicleSupport {

    private val playerVehicles = concurrentHashMapOf<Player, ProtocolPlayerVehicle>()

    init {
        everyAsync(10, 10) { playerVehicles.values.forEach(ProtocolVehicle::syncMounts) }
        on<PlayerTeleportEvent>(EventPriority.LOWEST) { playerVehicles[player]?.syncMounts()  }
        on<PlayerQuitEvent>(EventPriority.LOWEST) { playerVehicles.remove(player)?.clear() }
    }

    operator fun get(player: Player) =
        if (player.isOnline) playerVehicles.computeIfAbsent(player, ::ProtocolPlayerVehicle) else null

}

fun Player.mount(vararg entities: ProtocolEntity) = ProtocolPlayerVehicleSupport[this]?.mount(*entities)

fun Player.unmount(vararg entities: ProtocolEntity) = ProtocolPlayerVehicleSupport[this]?.unmount(*entities)