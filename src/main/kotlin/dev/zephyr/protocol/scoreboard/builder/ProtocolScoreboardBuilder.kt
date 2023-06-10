package dev.zephyr.protocol.scoreboard.builder

import dev.zephyr.extensions.filterValuesInInstance
import dev.zephyr.protocol.scoreboard.ProtocolScoreboard
import dev.zephyr.protocol.scoreboard.ScoreboardProtocol
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.entity.Player

@KotlinOpens
class ProtocolScoreboardBuilder() {
    constructor(block: ProtocolScoreboardBuilder.() -> Unit) : this() { block(this) }

    var title = "Scoreboard"
    val lines = mutableMapOf<Int, ProtocolScoreboardLine>()

    var invertIndexes = true
    val updaters = mutableSetOf<Pair<Int, ProtocolScoreboard.(Player) -> Unit>>()

    fun title(title: String) = apply { this.title = title }

    fun line(index: Int, line: ProtocolScoreboardLine) = apply { lines[index] = line }

    fun line(line: ProtocolScoreboardLine) = line((lines.keys.maxOrNull() ?: 0) + 1, line)

    fun line(index: Int, line: String) = line(index, StaticProtocolScoreboardLine(line))

    fun line(line: String) = line(StaticProtocolScoreboardLine(line))

    fun line(index: Int, interval: Int = 20, lineSupplier: (Player) -> String) =
        line(index, LazyProtocolScoreboardLine(interval, lineSupplier))

    fun line(interval: Int = 20, lineSupplier: (Player) -> String) =
        line(LazyProtocolScoreboardLine(interval, lineSupplier))

    fun line(index: Int) = line(index, EmptyProtocolScoreboardLine)

    fun line() = line(EmptyProtocolScoreboardLine)

    fun updater(interval: Int = 20, block: ProtocolScoreboard.(Player) -> Unit) =
        apply { updaters.add(interval to block) }

    fun create(player: Player): ProtocolScoreboard {
        return ProtocolScoreboard(title).apply {
            val lines = this@ProtocolScoreboardBuilder.lines.run {
                if (invertIndexes) {
                    val maxIndex = keys.max()
                    mapKeys { (index, _) -> maxIndex - index }
                } else this
            }

            title = this@ProtocolScoreboardBuilder.title

            lines.filterValuesInInstance<Int, StaticProtocolScoreboardLine>().forEach { (index, line) ->
                setLine(index, line.getContent(player))
            }

            lines.filterValuesInInstance<Int, LazyProtocolScoreboardLine>().forEach { (index, line) ->
                taskContext.everyAsync(0, line.interval) { setLine(index, line.getContent(player)) }
            }

            updaters.forEach { (interval, updater) ->
                taskContext.everyAsync(0, interval) { updater(this, player) }
            }

            ScoreboardProtocol[player] = this
            spawn(player)
        }
    }

    interface ProtocolScoreboardLine {

        fun getContent(player: Player): String

    }

    @KotlinOpens
    class StaticProtocolScoreboardLine(private val content: String) : ProtocolScoreboardLine {

        override fun getContent(player: Player) = content

    }

    object EmptyProtocolScoreboardLine : StaticProtocolScoreboardLine("")

    class LazyProtocolScoreboardLine(val interval: Int, val supplier: (Player) -> String) : ProtocolScoreboardLine {

        override fun getContent(player: Player) = supplier(player)

    }

}

fun scoreboard(block: ProtocolScoreboardBuilder.() -> Unit) =
    ProtocolScoreboardBuilder(block)