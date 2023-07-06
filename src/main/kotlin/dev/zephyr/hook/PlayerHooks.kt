package dev.zephyr.hook

import dev.zephyr.util.bukkit.craft
import dev.zephyr.util.bukkit.everyAsync
import dev.zephyr.util.bukkit.on
import dev.zephyr.util.bukkit.players
import dev.zephyr.util.java.tryAccessAndGet
import net.minecraft.network.syncher.DataWatcherObject
import net.minecraft.world.entity.player.EntityHuman
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent

object PlayerHooks {

    val AbsorptionDataWatcherObject = EntityHuman::class.java
        .declaredFields
        .first { it.type == DataWatcherObject::class.java }
        .tryAccessAndGet<DataWatcherObject<Float>>()

    fun hideHealth() {
        on<PlayerJoinEvent> { player.hideHealth() }
        everyAsync(20, 20) { players().forEach(Player::hideHealth) }
    }

}

fun Player.hideHealth() = craft().handle.aj().b(PlayerHooks.AbsorptionDataWatcherObject, -1_000_000_00f)