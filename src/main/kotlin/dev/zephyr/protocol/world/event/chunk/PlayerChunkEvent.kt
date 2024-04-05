package dev.zephyr.protocol.world.event.chunk

import dev.zephyr.protocol.world.ChunkPosition
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerEvent

@KotlinOpens
abstract class PlayerChunkEvent(player: Player, val position: ChunkPosition) : PlayerEvent(player) {

    val chunk by lazy(position::chunk)

}