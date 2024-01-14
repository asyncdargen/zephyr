package dev.zephyr.protocol.scoreboard

import dev.zephyr.protocol.scoreboard.builder.ProtocolScoreboardBuilder
import dev.zephyr.util.bukkit.on
import dev.zephyr.util.collection.concurrentHashMapOf
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object ScoreboardProtocol {

    lateinit var BoundedScoreboard: ProtocolScoreboardBuilder
    val PlayerScoreboards = concurrentHashMapOf<Player, ProtocolScoreboard>()

    init {
        on<PlayerJoinEvent> {
            if (this@ScoreboardProtocol::BoundedScoreboard.isInitialized) {
                BoundedScoreboard.create(player)
            }
        }
        on<PlayerQuitEvent> { remove(player) }
    }

    fun bind(builder: ProtocolScoreboardBuilder) {
        BoundedScoreboard = builder
    }

    operator fun set(player: Player, scoreboard: ProtocolScoreboard) =
        PlayerScoreboards.put(player, scoreboard)?.remove()

    operator fun get(player: Player) = PlayerScoreboards[player]

    fun remove(player: Player) = PlayerScoreboards.remove(player)?.remove()

}