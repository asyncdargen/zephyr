package dev.zephyr.protocol.world.chunk

import org.bukkit.Chunk
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerEvent

abstract class PlayerChunkEvent(player: Player, val chunk: Chunk) : PlayerEvent(player)