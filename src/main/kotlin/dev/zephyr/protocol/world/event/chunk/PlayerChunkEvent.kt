package dev.zephyr.protocol.world.event.chunk

import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Chunk
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerEvent

@KotlinOpens
abstract class PlayerChunkEvent(player: Player, val chunk: Chunk) : PlayerEvent(player)