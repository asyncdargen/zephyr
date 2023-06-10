package dev.zephyr.protocol.scoreboard

import dev.zephyr.extensions.concurrentHashMapOf
import dev.zephyr.extensions.on
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerQuitEvent

object ScoreboardProtocol {

    val PlayerScoreboards = concurrentHashMapOf<Player, ProtocolScoreboard>()

    init {
        on<PlayerQuitEvent> { remove(player) }
    }

    operator fun set(player: Player, scoreboard: ProtocolScoreboard) = PlayerScoreboards.put(player, scoreboard)?.remove()

    operator fun get(player: Player) = PlayerScoreboards[player]

    fun remove(player: Player) = PlayerScoreboards.remove(player)?.apply(ProtocolScoreboard::remove)

}