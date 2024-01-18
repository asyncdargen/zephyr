package dev.zephyr.protocol.scoreboard.builder

import dev.zephyr.protocol.scoreboard.ProtocolScoreboard
import dev.zephyr.protocol.scoreboard.ScoreboardProtocol
import dev.zephyr.protocol.scoreboard.builder.ProtocolScoreboardBuilder.LazyProtocolScoreboardLine
import dev.zephyr.protocol.scoreboard.builder.ProtocolScoreboardBuilder.StaticProtocolScoreboardLine
import dev.zephyr.util.collection.filterValuesIsInstance
import dev.zephyr.util.concurrent.threadLocal
import org.bukkit.entity.Player

class ProtocolScoreboardBuilderHolder<U>(
    val build: ProtocolScoreboardBuilder<U>.() -> Unit,
    val updaterEntity: (Player) -> U
) {

    val builderLocal = threadLocal { ProtocolScoreboardBuilder(updaterEntity).apply(build) }
    val builder get() = builderLocal.get()

    fun create(player: Player) = ProtocolScoreboard().apply {
        val scoreboardLines = builder.lines.run {
            if (builder.invertIndexes) {
                val maxIndex = keys.maxOrNull() ?: 0
                mapKeys { (index, _) -> maxIndex - index }
            } else this
        }

        title = builder.title

        scoreboardLines.filterValuesIsInstance<Int, StaticProtocolScoreboardLine>().forEach { (index, line) ->
            setLine(index, line.getContent(player))
        }

        scoreboardLines.filterValuesIsInstance<Int, LazyProtocolScoreboardLine>()
            .asSequence()
            .groupBy { it.value.interval }
            .forEach { (interval, lines) ->
                builder.updater(interval) { lines.forEach { (index, line) -> setLine(index, line.getContent(player)) } }
            }

        builder.updaters.groupBy { it.first }
            .forEach { (interval, updaters) ->
                taskContext.everyAsync(0, interval) { updaters.forEach { it.second(this, player) } }
            }

        ScoreboardProtocol[player] = this
        spawn(player)

        builderLocal.remove()
    }


    fun bind() = ScoreboardProtocol.bind(this)

}